package com.example.policy_service.service;

import com.example.policy_service.client.UserClient;
import com.example.policy_service.dto.PolicyDTO;
import com.example.policy_service.dto.PolicySearchData;
import com.example.policy_service.dto.PolicySearchRequest;
import com.example.policy_service.dto.userDto.SubscriberInfoDTO;
import com.example.policy_service.dto.userDto.UserDTO;
import com.example.policy_service.mapper.PolicyMapper;
import com.example.policy_service.message.*;
import com.example.policy_service.model.Policy;
import com.example.policy_service.repository.PolicyRepository;
import com.example.policy_service.spec.PolicySpecs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import com.example.policy_service.exception.ResourceNotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import com.example.policy_service.exception.PolicyNotFoundException;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PolicyService {

    @Autowired
    private PolicyMapper policyMapper;
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;
    @Autowired
    private UserClient userClient;
    private final String SUBSCRIBER_REQUEST_TOPIC = "policy.subscriber.request";
    private final String CAR_REQUEST_TOPIC = "policy.car.request";
    private final String SUBSCRIBER_RESPONSE_TOPIC = "policy.subscriber.response";
    private final String CAR_RESPONSE_TOPIC = "policy.car.response";

    private final Map<String, CompletableFuture<Page<PolicyDTO>>> futuresMap = new ConcurrentHashMap<>();
    private final Map<String, PolicySearchData> progressMap = new ConcurrentHashMap<>();
    private JsonSerializer<PolicySearchSubscriberRequestMessage> subscriberRequestMessageSerializer = new JsonSerializer<>();
    private JsonSerializer<PolicySearchCarRequestMessage> carRequestMessageSerializer = new JsonSerializer<>();

    public void getCarIdsByBrandOrModelOrYear(String correlationId, String brandName, String modelName, Integer year){
        PolicySearchCarRequestMessage message = new PolicySearchCarRequestMessage(correlationId, brandName, modelName, year);
        kafkaTemplate.send(CAR_REQUEST_TOPIC, carRequestMessageSerializer.serialize(CAR_REQUEST_TOPIC, message));
    }

    public CompletableFuture<Page<PolicyDTO>> search(Integer page, Integer size, String firstName, String lastName, String email, LocalDate date, String brandName, String modelName, Integer year){
        PageRequest pageRequest = PageRequest.of(page, size);

        String correlationId = UUID.randomUUID().toString();

        CompletableFuture<Page<PolicyDTO>> future = new CompletableFuture<>();
        futuresMap.put(correlationId, future);

        PolicySearchData searchData = new PolicySearchData();
        searchData.setDate(date);
        searchData.setPage(page);
        searchData.setSize(size);

        if(firstName==null && lastName==null && email==null){
            searchData.setHasUserServiceResponded(true);
        }
        if((brandName==null || brandName.isEmpty()) && (modelName==null || modelName.isEmpty()) && year==null){
            searchData.setHasCarServiceResponded(true);
        }

        progressMap.put(correlationId, searchData);

        if(!searchData.getHasUserServiceResponded()){
            getSubscriberIdsByFirstNameLastNameOrEmail(correlationId, firstName, lastName, email);
        }
        if(!searchData.getHasCarServiceResponded()){
            getCarIdsByBrandOrModelOrYear(correlationId, brandName, modelName, year);
        }
        if(searchData.isComplete()){
            completeSearch(correlationId, pageRequest);
        }

        return future;
    }

    @KafkaListener(topics = SUBSCRIBER_RESPONSE_TOPIC, groupId = "policy-service-group")
    public void listenForSubscriberResponse(String json){
        PolicySearchSubscriberResponseMessage responseMessage;
        try{
            responseMessage = new ObjectMapper().readValue(json, PolicySearchSubscriberResponseMessage.class);

            List<SubscriberInfoDTO> subscriberInfos = responseMessage.getSubscriberInfos();

            List<Long> ids = subscriberInfos.stream()
                    .map(SubscriberInfoDTO::getId)
                    .toList();

            String correlationId = responseMessage.getCorrelationId();
            PolicySearchData searchData = progressMap.get(correlationId);
            searchData.setHasUserServiceResponded(true);
            searchData.setSubscriberIds(ids);
            searchData.setSubscriberInfos(subscriberInfos);

            if(searchData.isComplete()){
                PageRequest pageRequest = PageRequest.of(searchData.getPage(), searchData.getSize());
                completeSearch(correlationId, pageRequest);
            }

        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = CAR_RESPONSE_TOPIC,
            groupId = "policy-service-group")
    public void listenForCarResponse(String json){
        PolicySearchCarResponseMessage responseMessage;
        try {
            responseMessage = new ObjectMapper().readValue(json, PolicySearchCarResponseMessage.class);
            List<Long> carIds = responseMessage.getCarIds();
            System.out.println("*********************************************************CARS"+carIds);
            String correlationId = responseMessage.getCorrelationId();
            PolicySearchData searchData = progressMap.get(correlationId);
            searchData.setHasCarServiceResponded(true);
            searchData.setCarIds(carIds);

            if(searchData.isComplete()){
                PageRequest pageRequest = PageRequest.of(searchData.getPage(), searchData.getSize());
                completeSearch(correlationId, pageRequest);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Page<PolicyDTO>> searchPolicies(PolicySearchRequest request) {
        return search(
                request.getPage(),
                request.getSize(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getDate(),
                request.getBrandName(),
                request.getModelName(),
                request.getCarYear()
        );
    }

    private void completeSearch(String correlationId, PageRequest pageRequest){
        PolicySearchData searchData = progressMap.remove(correlationId);
        CompletableFuture<Page<PolicyDTO>> future = futuresMap.remove(correlationId);
        LocalDate date = searchData.getDate();
        List<Long> carIds = searchData.getCarIds();
        List<Long> subscriberIds = searchData.getSubscriberIds();

        //ako ne postoji nijedan subscriber, ili nijedan auto koji odgovara uslovu, vrati praznu listu
        if ((carIds != null && carIds.isEmpty()) ||
                (subscriberIds != null && subscriberIds.isEmpty())) {
            future.complete(new PageImpl<>(Collections.emptyList(), pageRequest, 0));
            return;
        }
        Specification<Policy> spec = PolicySpecs.buildCriteria(date, carIds, subscriberIds);
        Page<Policy> policiesPage = policyRepository.findAll(spec, pageRequest);

        List<PolicyDTO> dtos = policyMapper.listToDTO(policiesPage.getContent());

        Map<Long, UserDTO> userMap = new HashMap<>();
        if(subscriberIds!=null){
            for (Long subId : subscriberIds){
                try {
                    UserDTO userDTO = userClient.getUserById(subId);
                    if (userDTO != null) {
                        userMap.put(subId, userDTO);
                    }
                } catch (Exception e) {
                    System.err.println("Sub with id "+ subId+ " error:"+e);
                }
            }
        }
        dtos.forEach(dto -> {
            UserDTO user = userMap.get(dto.getSubscriberId());
            if (user != null) {
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
            }
        });

        Page<PolicyDTO> pageResult = new PageImpl<>(
                dtos,
                policiesPage.getPageable(),
                policiesPage.getTotalElements()
        );
        if(future!=null){
            future.complete(pageResult);
        }
    }
    public void getSubscriberIdsByFirstNameLastNameOrEmail(String correlationId, String firstName, String lastName, String email){
        PolicySearchSubscriberRequestMessage message = new PolicySearchSubscriberRequestMessage(correlationId, firstName, lastName, email);
        kafkaTemplate.send(SUBSCRIBER_REQUEST_TOPIC, subscriberRequestMessageSerializer.serialize(SUBSCRIBER_REQUEST_TOPIC, message));
    }

    @Transactional
    public PolicyDTO getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        return policyMapper.toDto(policy);
    }

    @Transactional
    public Page<PolicyDTO> findAll(int page_num, int page_size){
        PageRequest pageRequest = PageRequest.of(page_num, page_size);

        Page<Policy> policyPage = policyRepository.findAll(pageRequest);


        List<PolicyDTO> dtos = policyPage.stream()
                .map(policyMapper::toDto)
                .collect(Collectors.toList());

        Set<Long> subscriberIds = dtos.stream()
                .map(PolicyDTO::getSubscriberId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, UserDTO> userMap = subscriberIds.stream()
                .map(id -> {
                    try {
                        return userClient.getUserById(id);
                    } catch (Exception e) {
                        System.err.println("Sub with id "+ id+ " error:"+e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserDTO::getId, u -> u));

        dtos.forEach(dto -> {
            UserDTO user = userMap.get(dto.getSubscriberId());
            if (user != null) {
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
            }
        });
        return new PageImpl<>(dtos, pageRequest, policyPage.getTotalElements());
    }

    @Transactional
    public Page<PolicyDTO> getPoliciesBySalesAgentId(Long salesAgentId, int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Policy> policiesPage = policyRepository.findByProposal_SalesAgentId(salesAgentId, pageRequest);
        if (policiesPage.isEmpty()) {
            throw new PolicyNotFoundException("No policies found for sales agent ID: " + salesAgentId);
        }

        List<PolicyDTO> dtos = policiesPage.stream()
                .map(policyMapper::toDto)
                .collect(Collectors.toList());

        Set<Long> subscriberIds = dtos.stream()
                .map(PolicyDTO::getSubscriberId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, UserDTO> userMap = subscriberIds.stream()
                .map(id -> {
                    try {
                        return userClient.getUserById(id);
                    } catch (Exception e) {
                        System.err.println("Sub with id "+ id+ " error:"+e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserDTO::getId, u -> u));

        dtos.forEach(dto -> {
            UserDTO user = userMap.get(dto.getSubscriberId());
            if (user != null) {
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
            }
        });
        return new PageImpl<>(dtos, pageRequest, policiesPage.getTotalElements());
    }

    public Page<PolicyDTO> getPolicies(
            Long subscriberId,
            int page,
            int size,
            String sortField,
            String sortDir) {

        sortField = "dateSigned";

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Policy> policiesPage = policyRepository.findAllByProposal_SubscriberId(subscriberId, pageable);
        List<Policy> policies = policiesPage.getContent();

        if (policies.isEmpty()) throw new ResourceNotFoundException("No policies for subscriber " + subscriberId);

        List<PolicyDTO> dtos = policies.stream().map(policyMapper::toDto).collect(Collectors.toList());

        return new PageImpl<>(dtos, policiesPage.getPageable(), policiesPage.getTotalElements());
    }
}


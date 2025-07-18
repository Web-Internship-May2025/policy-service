package com.example.policy_service.repository;

import com.example.policy_service.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long>, JpaSpecificationExecutor<Policy> {
    
    default Page<Set<Policy>> findAllAsSetPaginated(Pageable pageable) {

        Page<Policy> page = findAll(pageable);
        return page.map(entity -> Set.of(entity));
    }
    Page<Policy> findByProposal_SalesAgentId(Long salesAgentId, Pageable pageable);

    Page<Policy> findAllByProposal_SubscriberId(Long subscriberId, Pageable pageable);
}

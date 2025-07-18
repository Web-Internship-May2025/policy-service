package com.example.policy_service.dto;

import com.example.policy_service.dto.userDto.SubscriberInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySearchData {
    private String subscriberFirstName;
    private String subscriberLastName;
    private String subscriberEmail;
    private LocalDate date;
    private String brand;
    private String model;
    private Integer year;
    private Integer page = 0;
    private Integer size = 20;

    private Boolean hasUserServiceResponded = false;
    private Boolean hasCarServiceResponded = false;

    private List<Long> subscriberIds;
    private List<Long> carIds;

    private List<SubscriberInfoDTO> subscriberInfos;

    public boolean isComplete(){
        return hasUserServiceResponded && hasCarServiceResponded;
    }
}

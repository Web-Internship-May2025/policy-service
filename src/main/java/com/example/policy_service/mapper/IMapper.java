package com.example.policy_service.mapper;

import org.springframework.stereotype.Component;

@Component
public interface IMapper <E, D>{
    D toDto(E entity);
    E toEntity(D dto);
}

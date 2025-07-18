package com.example.policy_service.mapper;

import com.example.policy_service.dto.CountryDTO;
import com.example.policy_service.model.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper implements IMapper<Country, CountryDTO> {

    @Override
    public CountryDTO toDto(Country entity) {
        if (entity == null) {
            return null;
        }
        return new CountryDTO(
                entity.getId(),
                entity.getCountryCode(),
                entity.getName(),
                entity.getCarPlateNumberRegex()
        );
    }

    @Override
    public Country toEntity(CountryDTO dto) {
        if (dto == null) {
            return null;
        }
        Country country = new Country();
        country.setId(dto.getId());
        country.setCountryCode(dto.getCountryCode());
        country.setName(dto.getName());
        country.setCarPlateNumberRegex(dto.getCarPlateNumberRegex());
        return country;
    }
}
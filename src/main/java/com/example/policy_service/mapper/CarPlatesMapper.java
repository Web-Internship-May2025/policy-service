package com.example.policy_service.mapper;

import com.example.policy_service.dto.CarPlatesDTO;
import com.example.policy_service.model.CarPlates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarPlatesMapper implements IMapper<CarPlates, CarPlatesDTO> {

    @Autowired
    private final CountryMapper countryMapper = new CountryMapper();

    @Override
    public CarPlatesDTO toDto(CarPlates entity) {
        if (entity == null) {
            return null;
        }
        return new CarPlatesDTO(
                entity.getId(),
                countryMapper.toDto(entity.getCountry()),
                entity.getPlateNumber()
        );
    }

    @Override
    public CarPlates toEntity(CarPlatesDTO dto) {
        if (dto == null) {
            return null;
        }
        CarPlates entity = new CarPlates();
        entity.setId(dto.getId());
        entity.setCountry(countryMapper.toEntity(dto.getCountry()));
        entity.setPlateNumber(dto.getPlateNumber());
        return entity;
    }
}
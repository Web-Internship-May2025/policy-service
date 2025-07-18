package com.example.policy_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarPlates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
    @NotBlank
    @Column(nullable = false)
    private String plateNumber;

    @PrePersist
    @PreUpdate
    public void validateBeforeSave() {
        validate();
    }

    public void validate() {
        String pattern = country.getCarPlateNumberRegex();
        if (pattern == null) {
            throw new IllegalArgumentException("Country does not have a valid regex");
        }
        if (!plateNumber.matches(pattern)) {
            throw new IllegalArgumentException(
                    "Invalid plate format for country: " + country.getCountryCode());
        }
    }
}

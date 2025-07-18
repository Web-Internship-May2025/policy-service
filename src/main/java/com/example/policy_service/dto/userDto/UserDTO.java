package com.example.policy_service.dto.userDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\d{13}")
    private String jmbg;

    @NotNull
    @Past
    private LocalDateTime birthDate;

    private Boolean isDeleted;

    @NotNull
    private String gender;

    @NotNull
    private String maritalStatus;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Pattern(
            // 1) (?=.{8,}$)      ≥ 8 chars
            // 2) (?=.*[a-z])     ≥ 1 lowercase
            // 3) (?=.*[A-Z])     ≥ 1 uppercase
            // 4) (?=.*\d)        ≥ 1 digit
            // 5) (?=.*[!#@\$%\^&\*]) ≥ 1 special char
            // 6) (?!.*\s)        no whitespace anywhere
            regexp  = "^(?=.{8,}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!#@\\$%\\^&\\*])(?!.*\\s).*$",
            message = "Password must be ≥8 chars, include uppercase, lowercase, digit, special (!#@$%^&*), and contain no spaces or tabs"
    )
    private String password;
    private Boolean isEnabled;
    private Boolean isActive;

    @NotNull
    private String userRoleType;
}

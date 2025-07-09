package com.dre.gymapp.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrainerRegistrationRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "SpecializationId is required")
    private Long specializationId;

    public TrainerRegistrationRequest(String firstName, String lastName,  Long specializationId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specializationId = specializationId;
    }
}

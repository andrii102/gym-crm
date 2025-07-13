package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Request object for registering a new trainer user")
public class TrainerRegistrationRequest {
    @Schema(description = "Username that will be used to login to the system")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "First name of the trainer user")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "Last name of the trainer user")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "ID of the specialization that defines trainer's area of expertise")
    @NotNull(message = "SpecializationId is required")
    private Long specializationId;

    public TrainerRegistrationRequest(String firstName, String lastName,  Long specializationId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specializationId = specializationId;
    }
}

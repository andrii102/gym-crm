package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for registering a new trainee user")
public class TraineeRegistrationRequest {
    @Schema(description = "First name of the trainee user")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "Last name of the trainee user")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "Birth date of the trainee user")
    private LocalDate dateOfBirth;

    @Schema(description = "Physical address of the trainee user")
    private String address;
    public TraineeRegistrationRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

package com.dre.gymapp.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object containing updated trainee profile information")
public class TraineeProfileUpdateRequest {
    @Schema(description = "Username of the trainee")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "First name of the trainee")
    @NotBlank(message = "Firstname is required")
    private String firstName;

    @Schema(description = "Last name of the trainee")
    @NotBlank(message = "Lastname is required")
    private String lastName;

    @Schema(description = "Address of the trainee")
    private String address;

    @Schema(description = "Date of birth of the trainee")
    private LocalDate dateOfBirth;

    @Schema(description = "Active status of the trainee")
    @NotNull(message = "IsActive is required")
    private boolean isActive;

    public TraineeProfileUpdateRequest(String username, String firstName, String lastName, boolean isActive) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }
}

package com.dre.gymapp.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for updating trainer profile")
public class TrainerProfileUpdateRequest {
    @Schema(description = "Username of the trainer")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "First name of the trainer")
    @NotBlank(message = "Firstname is required")
    private String firstName;

    @Schema(description = "Last name of the trainer")
    @NotBlank(message = "Lastname is required")
    private String lastName;

    // read-only
    @Schema(description = "Type of training the trainer specializes in")
    @NotBlank(message = "TrainingType is required")
    private String trainingType;

    @Schema(description = "Active status of the trainer")
    @NotNull(message = "IsActive is required")
    private boolean isActive;
}

package com.dre.gymapp.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileUpdateRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Firstname is required")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    // read-only
    @NotBlank(message = "TrainingType is required")
    private String trainingType;

    @NotNull(message = "IsActive is required")
    private boolean isActive;
}

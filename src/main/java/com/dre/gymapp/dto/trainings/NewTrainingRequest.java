package com.dre.gymapp.dto.trainings;

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
@Schema(description = "Request object for creating a new training session")
public class NewTrainingRequest {
    @Schema(description = "Username of the trainee for the training session")
    @NotBlank(message = "Trainee username cannot be blank")
    private String traineeUsername;

    @Schema(description = "Username of the trainer conducting the training session")
    @NotBlank(message = "Trainer username cannot be blank")
    private String trainerUsername;

    @Schema(description = "Name of the training session")
    @NotBlank(message = "Training name cannot be blank")
    private String trainingName;

    @Schema(description = "Date when the training session will take place")
    @NotNull(message = "Training date cannot be null")
    private LocalDate trainingDate;

    @Schema(description = "Duration of the training session in minutes")
    @NotNull(message = "Training duration cannot be null")
    private Integer trainingDuration;
}

package com.dre.gymapp.dto.trainings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewTrainingRequest {
    @NotBlank(message = "Trainee username cannot be blank")
    private String traineeUsername;

    @NotBlank(message = "Trainer username cannot be blank")
    private String trainerUsername;

    @NotBlank(message = "Training name cannot be blank")
    private String trainingName;

    @NotNull(message = "Training date cannot be null")
    private LocalDate trainingDate;

    @NotNull(message = "Training duration cannot be null")
    private Integer trainingDuration;
}

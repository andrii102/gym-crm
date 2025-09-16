package com.dre.gymapp.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingEventRequest {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean active;
    private LocalDate trainingDate;
    private Integer trainingDuration;
    private String actionType;
}

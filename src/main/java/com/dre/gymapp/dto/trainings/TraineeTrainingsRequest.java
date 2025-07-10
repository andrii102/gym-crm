package com.dre.gymapp.dto.trainings;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TraineeTrainingsRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerUsername;
    private String trainingType;
}

package com.dre.gymapp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Training {
    String traineeId;
    String trainerId;
    String trainingName;
    TrainingType trainingType;
    LocalDate trainingDate;
    Integer trainingDuration;
}

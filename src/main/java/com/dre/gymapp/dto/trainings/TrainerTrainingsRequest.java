package com.dre.gymapp.dto.trainings;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TrainerTrainingsRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String traineeUsername;
}

package com.dre.gymapp.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeResponse {
    private Long trainingTypeId;
    private String trainingType;
}

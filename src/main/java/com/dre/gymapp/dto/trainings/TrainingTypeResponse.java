package com.dre.gymapp.dto.trainings;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing training type details")
public class TrainingTypeResponse {
    @Schema(description = "Unique identifier of the training type")
    private Long trainingTypeId;
    @Schema(description = "Name of the training type")
    private String trainingType;
}

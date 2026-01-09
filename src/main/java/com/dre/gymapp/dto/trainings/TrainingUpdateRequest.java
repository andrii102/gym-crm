package com.dre.gymapp.dto.trainings;

import com.dre.gymapp.model.TrainingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Request object for updating training")
public class TrainingUpdateRequest {
    @Schema(description = "Status of the training", example = "SCHEDULED")
    private TrainingStatus status;
}

package com.dre.gymapp.dto.trainings;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(description = "Request object for filtering trainee's training sessions")
public class TraineeTrainingsRequest {
    @Schema(description = "Start date for filtering training sessions")
    private LocalDate periodFrom;

    @Schema(description = "End date for filtering training sessions")
    private LocalDate periodTo;

    @Schema(description = "Username of the trainer to filter by")
    private String trainerUsername;

    @Schema(description = "Type of training to filter by")
    private String trainingType;
}

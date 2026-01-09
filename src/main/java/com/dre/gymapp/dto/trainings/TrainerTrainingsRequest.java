package com.dre.gymapp.dto.trainings;

import com.dre.gymapp.model.TrainingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Request object for filtering trainer's training sessions")
public class TrainerTrainingsRequest {
    @Schema(description = "Start date for filtering training sessions")
    private LocalDateTime periodFrom;

    @Schema(description = "End date for filtering training sessions")
    private LocalDateTime periodTo;

    @Schema(description = "Username of the trainee to filter by")
    private String traineeUsername;

    @Schema(description = "Type of training to filter by")
    private TrainingStatus trainingStatus;

    @Schema(description = "Maximum number of training sessions to return")
    private Integer limit;
}
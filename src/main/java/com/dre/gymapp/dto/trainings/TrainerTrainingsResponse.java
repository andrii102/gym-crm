package com.dre.gymapp.dto.trainings;

import com.dre.gymapp.model.TrainingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing trainer's training session details")
public class TrainerTrainingsResponse {
    @Schema(description = "Unique identifier of the training session")
    private Long trainingId;

    @Schema(description = "Name of the training session")
    private String trainingName;

    @Schema(description = "Date when the training session takes place")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime trainingDateTime;

    @Schema(description = "Type of training session")
    private String trainingType;

    @Schema(description = "Duration of training session in minutes")
    private Integer trainingDuration;

    @Schema(description = "Username of the trainee attending the session")
    private String traineeUsername;

    @Schema(description = "Status of the training session")
    private TrainingStatus status;
}
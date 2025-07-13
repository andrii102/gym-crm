package com.dre.gymapp.dto.trainings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing trainee's training session details")
public class TraineeTrainingsResponse {
    @Schema(description = "Name of the training session")
    private String trainingName;

    @Schema(description = "Date when the training session takes place")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate trainingDate;

    @Schema(description = "Type of training session")
    private String trainingType;

    @Schema(description = "Duration of training session in minutes")
    private Integer trainingDuration;

    @Schema(description = "Username of the trainer conducting the session")
    private String trainerUsername;
}
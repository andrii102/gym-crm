package com.dre.gymapp.dto.trainer;

import com.dre.gymapp.dto.trainee.TraineeShortProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing trainer profile information")
public class TrainerProfileResponse {
    @Schema(description = "First name of the trainer")
    private String firstName;

    @Schema(description = "Last name of the trainer")
    private String lastName;

    @Schema(description = "Type of training the trainer specializes in")
    private String trainingType;

    @Schema(description = "Active status of the trainer")
    private boolean isActive;

    @Schema(description = "List of trainees assigned to this trainer")
    private List<TraineeShortProfile> trainees;
}

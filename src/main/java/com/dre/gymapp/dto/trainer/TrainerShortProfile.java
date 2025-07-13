package com.dre.gymapp.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Short profile information for a trainer")
public class TrainerShortProfile {
    @Schema(description = "First name of the trainer")
    private String firstName;

    @Schema(description = "Last name of the trainer")
    private String lastName;

    @Schema(description = "Username of the trainer")
    private String username;

    @Schema(description = "Type of training the trainer specializes in")
    private String trainingType;
}

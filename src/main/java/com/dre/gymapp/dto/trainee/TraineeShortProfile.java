package com.dre.gymapp.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Short profile information for a trainee")
public class TraineeShortProfile {
    @Schema(description = "Trainee username")
    private String username;

    @Schema(description = "Trainee first name")
    private String firstName;

    @Schema(description = "Trainee last name")
    private String lastName;
}
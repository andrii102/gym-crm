package com.dre.gymapp.dto.trainee;


import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing trainee profile details")
public class TraineeProfileResponse {
    @Schema(description = "First name of the trainee")
    private String firstName;

    @Schema(description = "Last name of the trainee")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Birth date of the trainee")
    private LocalDate dateOfBirth;

    @Schema(description = "Address of the trainee")
    private String address;

    @Schema(description = "Indicates if the trainee account is active")
    private boolean isActive;
    private List<TrainerShortProfile> trainers;
}

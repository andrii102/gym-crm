package com.dre.gymapp.dto.trainer;

import com.dre.gymapp.dto.trainee.TraineeShortProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileResponse {
    private String firstName;
    private String lastName;
    private String trainingType;
    private boolean isActive;
    private List<TraineeShortProfile> trainees;
}

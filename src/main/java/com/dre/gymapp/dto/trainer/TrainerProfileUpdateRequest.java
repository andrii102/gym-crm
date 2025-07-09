package com.dre.gymapp.dto.trainer;

import com.dre.gymapp.model.TrainingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String username;
    private TrainingType specialization;
}

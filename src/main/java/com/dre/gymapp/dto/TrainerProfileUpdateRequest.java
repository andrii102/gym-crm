package com.dre.gymapp.dto;

import com.dre.gymapp.model.TrainingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerProfileUpdateRequest {
    String firstName;
    String lastName;
    String username;
    TrainingType specialization;
}

package com.dre.gymapp.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerShortProfile {
    private String firstName;
    private String lastName;
    private String username;
    private String trainingType;
}

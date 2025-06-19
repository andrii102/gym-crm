package com.dre.gymapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trainer extends User{
    TrainingType specialization;
    String userId;
}

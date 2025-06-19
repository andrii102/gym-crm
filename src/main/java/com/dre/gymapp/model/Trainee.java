package com.dre.gymapp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Trainee extends User{
    LocalDate dateOfBirth;
    String address;
    String userId;
}

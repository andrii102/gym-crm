package com.dre.gymapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TraineeProfileUpdateRequest {
    String firstName;
    String lastName;
    String username;
    String address;
    LocalDate dateOfBirth;
}

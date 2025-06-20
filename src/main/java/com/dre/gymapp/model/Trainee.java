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

    public Trainee(String userId, String firstName, String lastName) {
        super(firstName, lastName);
        this.userId = userId;
    }

    public Trainee(String userId, String firstName, String lastName, LocalDate dateOfBirth, String address) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "userId='" + getUserId() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }

}

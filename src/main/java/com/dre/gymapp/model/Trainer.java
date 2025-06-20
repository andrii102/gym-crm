package com.dre.gymapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trainer extends User{
    TrainingType specialization;
    String userId;

    public Trainer(String userId, String firstName, String lastName) {
        super(firstName, lastName);
        this.userId = userId;
    }

    public Trainer(String userId, String firstName, String lastName, TrainingType specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
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

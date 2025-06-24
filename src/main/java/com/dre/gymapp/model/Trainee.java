package com.dre.gymapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDate dateOfBirth;
    String address;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    User user;

    public Trainee() {
    }

    public Trainee(LocalDate dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }


    @Override
    public String toString() {
        return "Trainee{" +
                "id='" + id + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", user=" + user +
                '}';
    }
}

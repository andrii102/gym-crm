package com.dre.gymapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Trainee {
    @Id
    Long id;

    LocalDate dateOfBirth;

    String address;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Training> trainings = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "trainee_trainer",
        joinColumns = @JoinColumn(name = "trainee_id"),
        inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<Trainer> trainers = new ArrayList<>();

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

package com.dre.gymapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "training_type")
    TrainingType specialization;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToMany(mappedBy = "trainers") // field in Trainee entity
    private List<Trainee> trainees = new ArrayList<>();

    public Trainer() {

    }

    public Trainer(TrainingType specialization) {
        this.specialization = specialization;
    }

    public Trainer(TrainingType specialization, User user) {
        this.specialization = specialization;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization=" + specialization.trainingTypeName +
                ", id=" + id +
                ", userId=" + user.getId() +
                '}';
    }
}

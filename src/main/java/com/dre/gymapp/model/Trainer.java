package com.dre.gymapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Trainer {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "training_type")
    TrainingType specialization;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    User user;

    public Trainer() {

    }

}

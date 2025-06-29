package com.dre.gymapp.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "trainingType")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String trainingTypeName;

    public TrainingType() {}

    public TrainingType(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}

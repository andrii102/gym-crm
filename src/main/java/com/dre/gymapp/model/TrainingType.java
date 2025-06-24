package com.dre.gymapp.model;

import jakarta.persistence.*;

@Entity
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String trainingTypeName;

    public TrainingType() {}
}

package com.dre.gymapp.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "trainingType")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_seq")
    @SequenceGenerator(name = "type_seq", sequenceName = "trainingType_SEQ", allocationSize = 1)
    Long id;
    @Column(nullable = false, unique = true)
    String trainingTypeName;

    public TrainingType() {}

    public TrainingType(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}

package com.dre.gymapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_seq")
    @SequenceGenerator(name = "training_seq", sequenceName = "Training_SEQ", allocationSize = 1)
    Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    Trainer trainer;

    @Column(nullable = false)
    String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type")
    TrainingType trainingType;

    @Column(nullable = false)
    LocalDateTime trainingDateTime;

    @Column(nullable = false)
    Integer trainingDuration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TrainingStatus status;

    public Training() {

    }

    public Training(Trainee trainee, Trainer trainer, String trainingName,
                    TrainingType trainingType, LocalDateTime trainingDateTime, Integer trainingDuration, TrainingStatus status) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDateTime = trainingDateTime;
        this.trainingDuration = trainingDuration;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainee=" + trainee +
                ", trainer=" + trainer +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDateTime +
                ", trainingDuration=" + trainingDuration +
                ", status=" + status +
                '}';
    }
}

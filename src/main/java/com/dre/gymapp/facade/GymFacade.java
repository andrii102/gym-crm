package com.dre.gymapp.facade;

import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import com.dre.gymapp.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private static final Logger logger = LoggerFactory.getLogger(GymFacade.class);

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public void printAllTrainees() {
        List<Trainee> trainees = traineeService.getAllTrainees();
        System.out.println("All Trainees:");
        if (trainees.isEmpty()) {
            System.out.println("No trainees found");
        } else {
            trainees.forEach(System.out::println);
        }
    }

    public void printAllTrainers() {
        List<Trainer> trainers = trainerService.getAllTrainers();
        System.out.println("All Trainers:");
        if (trainers.isEmpty()) {
            System.out.println("No trainers found");
        } else {
            trainers.forEach(System.out::println);
        }
    }

    public void printAllTrainings() {
        List<com.dre.gymapp.model.Training> trainings = trainingService.getAllTrainings();
        System.out.println("All Trainings:");
        if (trainings.isEmpty()) {
            System.out.println("No trainings found");
        } else {
            trainings.forEach(System.out::println);
        }
    }
}


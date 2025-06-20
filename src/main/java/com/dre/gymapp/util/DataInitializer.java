package com.dre.gymapp.util;

import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Component
public class DataInitializer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${trainees.file}")
    private String traineesFilePath;

    @Value("${trainers.file}")
    private String trainersFilePath;

    private TraineeService traineeService;
    private TrainerService trainerService;

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Initializing data");
        loadTrainees();
        loadTrainers();
    }

    public void loadTrainees() {
        readDataAndProcess(traineesFilePath, data ->
                traineeService.createTrainee(new Trainee(data[0], data[1], data[2])), "trainees");
    }

    public void loadTrainers() {
        readDataAndProcess(trainersFilePath, data ->
                trainerService.createTrainer(new Trainer(data[0], data[1], data[2])), "trainers");

    }

    public void readDataAndProcess(String filePath, Consumer<String[]> consumer, String entityType) {
        logger.info("Reading {} file: {}", entityType, filePath);

        try {
            Path path = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());
            try (BufferedReader br = Files.newBufferedReader(path)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    consumer.accept(data);
                }
            }
        } catch (Exception e) {
            logger.warn("Error while reading {} file: {}", entityType, e.getMessage());
        }
    }
}
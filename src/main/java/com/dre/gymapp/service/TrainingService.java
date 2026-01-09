package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.dao.TrainingDao;
import com.dre.gymapp.dao.TrainingTypeDao;
import com.dre.gymapp.dto.trainings.*;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final TrainingDao trainingDao;
    private final TrainingTypeDao trainingTypeDao;
    private final TrainingProducer trainingProducer;
    private final JmsTemplate jmsTemplate;

    public TrainingService(TraineeDao traineeDao, TrainerDao trainerDao, TrainingDao trainingDao, TrainingTypeDao trainingTypeDao, TrainingProducer trainingProducer, JmsTemplate jmsTemplate) {
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.trainingDao = trainingDao;
        this.trainingTypeDao = trainingTypeDao;
        this.trainingProducer = trainingProducer;
        this.jmsTemplate = jmsTemplate;
    }

    // Creates and saves a new training
    public Training createTraining(NewTrainingRequest request) {
        logger.info("Creating new training");
        Trainee trainee = traineeDao.findByUsername(request.getTraineeUsername()).orElseThrow(() -> new NotFoundException("Trainee not found"));
        Trainer trainer = trainerDao.findByUsername(request.getTrainerUsername()).orElseThrow(() -> new NotFoundException("Trainer not found"));

        Training training = new Training(trainee, trainer, request.getTrainingName(),
                trainer.getSpecialization(), request.getTrainingDateTime(), request.getTrainingDuration(), request.getStatus());
        trainingDao.save(training);

        TrainingEventRequest workloadRequest = new TrainingEventRequest(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().isActive(),
                training.getTrainingDateTime().toLocalDate(),
                training.getTrainingDuration(),
                "ADD"
        );

        trainingProducer.send("trainings.queue", workloadRequest);
        return training;
    }

    // Gets a training by its ID, throws exception if not found
    public Training getTrainingById(Long id) {
        logger.info("Getting training with ID: {}", id);
        try {
            return trainingDao.findById(id).orElseThrow(() -> new NotFoundException("Training not found"));
        } catch (NotFoundException e) {
            logger.warn("Training with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Gets training details and maps to TrainingResponse DTO
    public TrainingResponse getTrainingDetailsById(Long id) {
        Optional<Training> training = trainingDao.findById(id);
        if (training.isEmpty()) {
            throw new NotFoundException("Training not found");
        }

        return new TrainingResponse(
                training.get().getId(),
                training.get().getTrainingName(),
                training.get().getTrainingDateTime(),
                training.get().getTrainingType().getTrainingTypeName(),
                training.get().getTrainingDuration(),
                training.get().getTrainee().getUser().getUsername(),
                training.get().getTrainer().getUser().getUsername(),
                training.get().getStatus()
        );
    }

    // Gets a list of all trainings
    public List<Training> getAllTrainings() {
        logger.info("Getting all trainings");
        return trainingDao.findAll();
    }

    public List<TrainingTypeResponse> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeDao.findAll();
        List<TrainingTypeResponse> dto = trainingTypes.stream()
                .map(trainingType -> new TrainingTypeResponse(
                        trainingType.getId(),
                        trainingType.getTrainingTypeName()
                )).toList();
        return dto;
    }

    // Returns list of trainings by parameters
    public List<Training> getTrainingsByParams(String trainerUsername, String traineeUsername, LocalDateTime fromDateTime,
                                               LocalDateTime toDateTime, String trainingTypeName, TrainingStatus trainingStatus, Integer limit) {
        logger.info("Getting training by params");
        return trainingDao.findTrainingsByParams(trainerUsername, traineeUsername, fromDateTime, toDateTime, trainingTypeName, trainingStatus, limit);
    }

    // Remove trainings in trainer-workload-service
    public void removeTrainingsFromWorkload(List<Training> trainings) {
        for (Training training : trainings) {
            TrainingEventRequest workloadRequest = new TrainingEventRequest(
                    training.getTrainer().getUser().getUsername(),
                    training.getTrainer().getUser().getFirstName(),
                    training.getTrainer().getUser().getLastName(),
                    training.getTrainer().getUser().isActive(),
                    training.getTrainingDateTime().toLocalDate(),
                    training.getTrainingDuration(),
                    "DELETE"
            );
            jmsTemplate.convertAndSend("trainings.queue", workloadRequest);
        }
    }

    public void updateTrainingStatus(Long id, TrainingUpdateRequest updateRequest) {
        Training training = trainingDao.findById(id).orElseThrow(() -> new NotFoundException("Training not found"));

        if (updateRequest.getStatus() != null) {
            try {
                training.setStatus(updateRequest.getStatus());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid training status: {}", updateRequest.getStatus());
                throw new IllegalArgumentException("Invalid training status: " + updateRequest.getStatus());
            }
        }

        trainingDao.update(training);
    }
}


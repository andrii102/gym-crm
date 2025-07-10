package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TraineeRegistrationRequest;
import com.dre.gymapp.dto.trainee.TraineeProfileResponse;
import com.dre.gymapp.dto.trainee.TraineeProfileUpdateRequest;
import com.dre.gymapp.dto.trainee.UpdateTrainersListRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.dto.trainings.TraineeTrainingsRequest;
import com.dre.gymapp.dto.trainings.TraineeTrainingsResponse;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.Training;
import com.dre.gymapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    private final TraineeDao traineeDao;
    private final UserService userService;
    private final TrainingService trainingService;
    private final TrainerDao trainerDao;

    // DI
    @Autowired
    public TraineeService(TraineeDao traineeDao, UserService userService,
                          TrainingService trainingService, TrainerDao trainerDao) {
        this.traineeDao = traineeDao;
        this.userService = userService;
        this.trainingService = trainingService;
        this.trainerDao = trainerDao;
    }

    // Creates and saves a new trainee
    public RegistrationResponse createTrainee(TraineeRegistrationRequest request) {
        logger.info("Creating new trainee");

        User user = userService.createUser(request.getFirstName(), request.getLastName());

        Trainee trainee = new Trainee(request.getDateOfBirth(), request.getAddress());
        trainee.setUser(user);
        traineeDao.save(trainee);

        logger.info("Trainee created successfully");

        return new RegistrationResponse(user.getUsername(), user.getPassword());
    }

    // Updates an existing trainee record
    @Transactional
    public TraineeProfileResponse updateTrainee(String username, TraineeProfileUpdateRequest request) {
        logger.info("Updating trainee");
        User user = userService.getUserByUsername(username);
        Trainee trainee = traineeDao.findByUserId(
                user.getId()).orElseThrow(() -> new NotFoundException("Trainee not found"));

        if (request.getFirstName() != null) { user.setFirstName(request.getFirstName());}
        if (request.getLastName() != null) { user.setLastName(request.getLastName());}
        user.setActive(request.isActive());

        if (request.getDateOfBirth() != null) { trainee.setDateOfBirth(request.getDateOfBirth());}
        if (request.getAddress() != null) { trainee.setAddress(request.getAddress());}

        user = userService.updateUser(user);
        trainee = traineeDao.update(trainee);

        List<TrainerShortProfile> trainers = trainee.getTrainers().stream()
                        .map(trainer -> new TrainerShortProfile(
                                trainer.getUser().getFirstName(),
                                trainer.getUser().getLastName(),
                                trainer.getUser().getUsername(),
                                trainer.getSpecialization().getTrainingTypeName()
                        ))
                                .toList();

        logger.info("Trainee updated successfully");
        return new TraineeProfileResponse(
                user.getFirstName(),
                user.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                user.isActive(),
                trainers
        );
    }

    // Change password
    public void changePassword(String username, String password, String newPassword) {
        logger.info("Changing password for trainee with username: {}", username);
        User user = userService.authenticateUser(username, password);
        userService.changePassword(user, newPassword);
        logger.info("Password changed successfully");
    }

    // Gets a trainee by their ID, throws exception if not found
    public Trainee getTraineeById( Long id) {
        logger.info("Getting trainee with ID: {}", id);
        try {
            return traineeDao.findById(id).orElseThrow(() -> new NotFoundException("Trainee not found"));
        } catch (NotFoundException e) {
            logger.warn("Trainee with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Gets trainee by username
    public Trainee getTraineeByUsername(String username) {
        logger.info("Getting trainee with username: {}", username);
        User user = userService.getUserByUsername(username);
        Trainee trainee = traineeDao.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Trainee not found"));
        logger.info("Trainee retrieved successfully");
        return trainee;
    }

    // Gets trainee profile fy username
    @Transactional(readOnly = true)
    public TraineeProfileResponse getTraineeProfileByUsername(String username) {
        logger.info("Getting trainee profile with username: {}", username);
        Trainee trainee = traineeDao.findByUsername(username).orElseThrow(() -> new NotFoundException("Trainee not found"));
        List<TrainerShortProfile> trainers = trainee.getTrainers().stream()
                        .map(trainer -> new TrainerShortProfile(
                                trainer.getUser().getUsername(),
                                trainer.getUser().getFirstName(),
                                trainer.getUser().getLastName(),
                                trainer.getSpecialization().getTrainingTypeName()
                        )).toList();
        logger.info("Trainee profile retrieved successfully");
        return new TraineeProfileResponse(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().isActive(),
                trainers
        );
    }

    // Gets a list of all trainees
    public List<Trainee> getAllTrainees(String username, String password) {
        logger.info("Getting all trainees");
        userService.authenticateUser(username, password);
        return traineeDao.findAll();
    }

    // Deletes a trainee by their username
    public void deleteTraineeByUsername(String username){
        logger.info("Deleting trainee with username: {}", username);
        Trainee trainee = traineeDao.findByUsername(username).orElseThrow(() -> new NotFoundException("Trainee not found"));
        traineeDao.delete(trainee);
    }

    // Deletes a trainee by their ID
    public void deleteTraineeById (Long id) {
        logger.info("Deleting trainee with ID: {}", id);
        try {
            traineeDao.deleteById(id);
        } catch (NotFoundException e) {
            logger.warn("Trainee with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Updates trainee's trainer list
    @Transactional
    public List<TrainerShortProfile> updateTraineeTrainerList(String username, UpdateTrainersListRequest trainerList) {
        logger.info("Adding trainer to trainee with username: {}", username);
        Trainee trainee = traineeDao.findByUsername(username).orElseThrow(() -> new NotFoundException("Trainee not found"));
        List<Trainer> trainers = trainerDao.findByUsernames(trainerList.getTrainers());
        trainee.setTrainers(trainers);
        Trainee updatedTrainee = traineeDao.update(trainee);
        List<TrainerShortProfile> trainerShortProfiles = updatedTrainee.getTrainers().stream()
                .map(trainer -> new TrainerShortProfile(
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        trainer.getUser().getUsername(),
                        trainer.getSpecialization().getTrainingTypeName()
                ))
                .toList();

        logger.info("Trainer added successfully");
        return trainerShortProfiles;
    }

    // Returns trainee's trainings by params
    public List<TraineeTrainingsResponse> getTraineeTrainings(String username, TraineeTrainingsRequest request) {
        logger.info("Getting trainee trainings for trainee with username: {}", username);
        List<Training> trainings = trainingService.getTrainingsByParams(request.getTrainerUsername(), username,
                request.getPeriodFrom(), request.getPeriodTo(), request.getTrainingType());
        System.out.println("REQUEST:" + request);
        List<TraineeTrainingsResponse> dto = trainings.stream()
                .map(training -> new TraineeTrainingsResponse(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainer().getUser().getUsername()
                )).toList();

        logger.info("Trainee trainings retrieved successfully");
        return dto;
    }

    // Activates trainee
    public void activateTrainee(String username, String password) {
        logger.info("Activating trainee with username: {}", username);
        User user = userService.authenticateUser(username, password);
        userService.setActive(user, true);
        logger.info("Trainee activated successfully");
    }

    // De-activates trainee
    public void deactivateTrainee(String username, String password) {
        logger.info("Deactivating trainee with username: {}", username);
        User user = userService.authenticateUser(username, password);
        userService.setActive(user, false);
        logger.info("Trainee deactivated successfully");
    }
}

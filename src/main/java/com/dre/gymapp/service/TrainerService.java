package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.dao.TrainingTypeDao;
import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TrainerRegistrationRequest;
import com.dre.gymapp.dto.trainee.TraineeShortProfile;
import com.dre.gymapp.dto.trainee.UpdateTrainersListRequest;
import com.dre.gymapp.dto.trainer.TrainerProfileResponse;
import com.dre.gymapp.dto.trainer.TrainerProfileUpdateRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.dto.trainings.TrainerTrainingsRequest;
import com.dre.gymapp.dto.trainings.TrainerTrainingsResponse;
import com.dre.gymapp.dto.user.GeneratedUser;
import com.dre.gymapp.exception.BadRequestException;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.Training;
import com.dre.gymapp.model.User;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    private final TrainerDao trainerDao;
    private final UserService userService;
    private final TrainingTypeDao trainingTypeDao;
    private final TrainingService trainingService;
    private final TraineeDao traineeDao;

    // DI
    @Autowired
    public TrainerService(TrainerDao trainerDao, UserService userService,
                          TrainingTypeDao trainingTypeDao, TrainingService trainingService, TraineeDao traineeDao) {
        this.trainerDao = trainerDao;
        this.userService = userService;
        this.trainingTypeDao = trainingTypeDao;
        this.trainingService = trainingService;
        this.traineeDao = traineeDao;
    }

    // Creates and saves a new trainer
    public RegistrationResponse createTrainer(TrainerRegistrationRequest request) {
        logger.info("Creating new trainer");

        GeneratedUser generatedUser = userService.createUser(request.getFirstName(), request.getLastName());

        Trainer trainer = new Trainer(trainingTypeDao.findById(request.getSpecializationId()), generatedUser.getUser());
        trainerDao.save(trainer);

        logger.info("Trainer created successfully");

        return new RegistrationResponse(generatedUser.getUser().getUsername(), generatedUser.getRawPassword());
    }

    // Gets a trainer by their ID, throws exception if not found
    @Deprecated // method is used only in tests
    public Trainer getTrainerById(Long id) {
        logger.info("Getting trainer with ID: {}", id);
        try {
            return trainerDao.findById(id).orElseThrow(() -> new NotFoundException("Trainer not found"));
        } catch (NotFoundException e) {
            logger.warn("Trainer with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    @Deprecated // method is used only in tests
    public Trainer getTrainerByUsername(String username) {
        logger.info("Getting trainer with username: {}", username);
        User user = userService.getUserByUsername(username);
        Trainer trainer = trainerDao.findById(user.getId()).orElseThrow(() -> new NotFoundException("Trainer not found"));
        logger.info("Trainer retrieved successfully");
        return trainer;
    }

    @Transactional(readOnly = true)
    public TrainerProfileResponse getTrainerProfileByUsername(String username) {
        logger.info("Getting trainer profile with username: {}", username);
        User user = userService.getUserByUsername(username);
        Trainer trainer = trainerDao.findById(user.getId()).orElseThrow(() -> new NotFoundException("Trainer not found"));
        List<TraineeShortProfile> trainees = trainer.getTrainees().stream()
                .map(trainee -> new TraineeShortProfile(
                        trainee.getUser().getUsername(),
                        trainee.getUser().getFirstName(),
                        trainee.getUser().getLastName()
                )).toList();

        logger.info("Trainer profile retrieved successfully");
        return new TrainerProfileResponse(
                user.getFirstName(),
                user.getLastName(),
                trainer.getSpecialization().getTrainingTypeName(),
                user.isActive(),
                trainees
        );
    }

    // Gets a list of all trainers
    @Deprecated // method is used only in tests
    public List<Trainer> getAllTrainers() {
        logger.info("Getting all trainers");
        return trainerDao.findAll();
    }

    // Updates an existing trainer record
    @Transactional
    public TrainerProfileResponse updateTrainer(String username, TrainerProfileUpdateRequest request) {
        logger.info("Updating trainer with username: {}", username);
        User user = userService.getUserByUsername(username);
        Trainer trainer = trainerDao.findById(user.getId()).orElseThrow(() -> new NotFoundException("Trainer not found"));

        if (!trainer.getSpecialization().getTrainingTypeName().equals(request.getTrainingType())){
            logger.warn("Cannot change training type for trainer with username: {}", username);
            throw new BadRequestException("Cannot change training type for trainer");
        }

        if (request.getFirstName() != null) { user.setFirstName(request.getFirstName());}
        if (request.getLastName() != null) { user.setLastName(request.getLastName());}
        user.setActive(request.isActive());

        user = userService.updateUser(user);
        trainer = trainerDao.update(trainer);

        List<TraineeShortProfile> trainees = trainer.getTrainees().stream()
                        .map(trainee -> new TraineeShortProfile(
                                trainee.getUser().getUsername(),
                                trainee.getUser().getFirstName(),
                                trainee.getUser().getLastName()
                        )).toList();

        logger.info("Trainer updated successfully");
        return new TrainerProfileResponse(
                user.getFirstName(),
                user.getLastName(),
                trainer.getSpecialization().getTrainingTypeName(),
                user.isActive(),
                trainees
        );
    }

    // Returns list of trainers
    @Deprecated // method is used only in tests
    public List<Trainer> getTrainersByUsernames(UpdateTrainersListRequest trainerList) {
        logger.info("Getting trainers by usernames");
        List<Trainer> trainers = trainerDao.findByUsernames(trainerList.getTrainers());
        logger.info("Trainers retrieved successfully");
        return trainers;
    }

    // Returns list with unassigned trainers
    public List<TrainerShortProfile> getUnassignedTrainers(String username){
        logger.info("Getting unassigned trainers for trainee with username '{}'", username);
        Trainee trainee = traineeDao.findByUsername(username).orElseThrow(() -> new NotFoundException("Trainee not found"));
        List<Trainer> trainers = trainerDao.findUnassignedTrainersOnTrainee(trainee.getId());
        List<TrainerShortProfile> traineeShortProfiles = trainers.stream()
                .map(trainer -> new TrainerShortProfile(
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        trainer.getUser().getUsername(),
                        trainer.getSpecialization().getTrainingTypeName()
                )).toList();
        return traineeShortProfiles;
    }

    public List<TrainerTrainingsResponse> getTrainerTrainings(String username, TrainerTrainingsRequest request) {
        logger.info("Getting trainer trainings for trainer with username: {}", username);
        List<Training> trainings = trainingService.getTrainingsByParams(username, request.getTraineeUsername(),
                request.getPeriodFrom(), request.getPeriodTo(), null);
        List<TrainerTrainingsResponse> dto = trainings.stream()
                .map(training -> new TrainerTrainingsResponse(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainee().getUser().getUsername()
                )).toList();

        logger.info("Trainer trainings retrieved successfully");
        return dto;
    }

    // Sets trainer active status
    public void setTrainerActiveStatus(String username, @NotNull(message = "Active is required") boolean active) {
        logger.info("Setting trainer active status for trainer with username: {}", username);
        User user = userService.getUserByUsername(username);
        userService.setActive(user, active);
        logger.info("Trainer active status set successfully");
    }
}

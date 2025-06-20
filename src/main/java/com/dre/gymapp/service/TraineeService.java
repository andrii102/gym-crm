package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.util.CredentialsGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    private TraineeDao traineeDao;
    private CredentialsGenerator credentialsGenerator;

    // Sets the trainee DAO through dependency injection
    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    // Sets the Credential Generator through dependency injection
    @Autowired
    public void setCredentialsGenerator(CredentialsGenerator credentialsGenerator) {
        this.credentialsGenerator = credentialsGenerator;
    }

    // Creates and saves a new trainee
    public Trainee createTrainee(Trainee trainee) {
        logger.info("Creating new trainee with ID: {}", trainee.getUserId());

        String username = credentialsGenerator.generateUsername(trainee.getFirstName(), trainee.getLastName(),
                traineeDao::usernameExists);
        String password = credentialsGenerator.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);

        return traineeDao.save(trainee);
    }

    // Updates an existing trainee record
    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Updating trainee with ID: {}", trainee.getUserId());
        return traineeDao.update(trainee);
    }

    // Gets a trainee by their ID, throws exception if not found
    public Trainee getTraineeById(String id) {
        logger.info("Getting trainee with ID: {}", id);
        try {
            return traineeDao.findById(id).orElseThrow(() -> new NotFoundException("Trainee not found"));
        } catch (NotFoundException e) {
            logger.warn("Trainee with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Gets a list of all trainees
    public List<Trainee> getAllTrainees() {
        logger.info("Getting all trainees");
        return traineeDao.findAll();
    }

    // Deletes a trainee by their ID
    public void deleteTraineeById(String id) {
        logger.info("Deleting trainee with ID: {}", id);
        traineeDao.deleteById(id);
    }
}

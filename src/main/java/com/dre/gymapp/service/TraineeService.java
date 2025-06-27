package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.dto.TraineeProfileUpdateRequest;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    private TraineeDao traineeDao;
    private UserService userService;

    // Sets the trainee DAO through dependency injection
    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // Creates and saves a new trainee
    public Trainee createTrainee(String firstName, String lastName) {
        logger.info("Creating new trainee");

        User user = userService.createUser(firstName, lastName);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        traineeDao.save(trainee);

        logger.info("Trainee created successfully");

        return trainee;
    }

    // Updates an existing trainee record
    public Trainee updateTrainee(String username, String password, TraineeProfileUpdateRequest request) {
        logger.info("Updating trainee");
        User user = userService.authenticateUser(username, password);
        Trainee trainee = traineeDao.findById(user.getId()).orElseThrow(() -> new NotFoundException("Trainee not found"));

        if (request.getFirstName() != null) { user.setFirstName(request.getFirstName());}
        if (request.getLastName() != null) { user.setLastName(request.getLastName());}
        if (request.getUsername() != null) { user.setUsername(request.getUsername());}

        if (request.getDateOfBirth() != null) { trainee.setDateOfBirth(request.getDateOfBirth());}
        if (request.getAddress() != null) { trainee.setAddress(request.getAddress());}

        userService.updateUser(user);
        return traineeDao.update(trainee);
    }

    // Change password
    public void changePassword(String username, String password, String newPassword) {
        logger.info("Changing password for trainee with username: {}", username);
        User user = userService.authenticateUser(username, password);
        userService.changePassword(user, newPassword);
        logger.info("Password changed successfully");
    }

    // Gets a trainee by their ID, throws exception if not found
    public Trainee getTraineeById(String username, String password, Long id) {
        logger.info("Getting trainee with ID: {}", id);
        userService.authenticateUser(username, password);
        try {
            return traineeDao.findById(id).orElseThrow(() -> new NotFoundException("Trainee not found"));
        } catch (NotFoundException e) {
            logger.warn("Trainee with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Gets a list of all trainees
    public List<Trainee> getAllTrainees(String username, String password) {
        logger.info("Getting all trainees");
        userService.authenticateUser(username, password);
        return traineeDao.findAll();
    }

    // Deletes a trainee by their username
    public void deleteTraineeByUsername(String username, String password, String traineeUsername){
        logger.info("Deleting trainee with username: {}", traineeUsername);
        User user = userService.authenticateUser(username, password);
        userService.deleteUserById(user.getId());
        traineeDao.deleteById(user.getId());
    }

    // Deletes a trainee by their ID
    public void deleteTraineeById (String username, String password, Long id) {
        logger.info("Deleting trainee with ID: {}", id);
        userService.authenticateUser(username, password);
        try {
            traineeDao.deleteById(id);
        } catch (NotFoundException e) {
            logger.warn("Trainee with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Adds trainer to the list of trainers
    public void addTrainer(String username, String password,Trainee trainee, Trainer trainer) {
        logger.info("Adding trainer to trainee with username: {}", username);
        User user = userService.authenticateUser(username, password);
        trainee.getTrainers().add(trainer);
        traineeDao.update(trainee);
        logger.info("Trainer added successfully");
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

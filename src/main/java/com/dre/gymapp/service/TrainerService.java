package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.dto.TrainerProfileUpdateRequest;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    private TrainerDao trainerDao;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // Sets the trainer DAO through dependency injection
    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    // Creates and saves a new trainer
    public Trainer createTrainer(String firstName, String lastName) {
        logger.info("Creating new trainer");

        User user = userService.createUser(firstName, lastName);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainerDao.save(trainer);

        logger.info("Trainer created successfully");

        return trainer;
    }

    public void changePassword(String username, String password, String newPassword) {
        logger.info("Changing password for trainer with username: {}", username);
        User user = userService.authenticateUser(username, password);
        userService.changePassword(user, newPassword);
        logger.info("Password changed successfully");
    }

    // Gets a trainer by their ID, throws exception if not found
    public Trainer getTrainerById(String username, String password, Long id) {
        logger.info("Getting trainer with ID: {}", id);
        userService.authenticateUser(username, password);
        try {
            return trainerDao.findById(id).orElseThrow(() -> new NotFoundException("Trainer not found"));
        } catch (NotFoundException e) {
            logger.warn("Trainer with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Gets a list of all trainers 
    public List<Trainer> getAllTrainers(String username, String password) {
        logger.info("Getting all trainers");
        userService.authenticateUser(username, password);
        return trainerDao.findAll();
    }

    // Updates an existing trainer record
    public Trainer updateTrainer(String username, String password, TrainerProfileUpdateRequest request) {
        logger.info("Updating trainer");
        User user = userService.authenticateUser(username, password);
        Trainer trainer = trainerDao.findById(user.getId()).orElseThrow(() -> new NotFoundException("Trainer not found"));

        if (request.getFirstName() != null) { user.setFirstName(request.getFirstName());}
        if (request.getLastName() != null) { user.setLastName(request.getLastName());}
        if (request.getUsername() != null) { user.setUsername(request.getUsername());}

        if (request.getSpecialization() != null) { trainer.setSpecialization(request.getSpecialization());}

        userService.updateUser(user);
        return trainerDao.update(trainer);
    }

    // Returns list with unassigned trainers
    public List<Trainer> findUnassignedTrainers(String username, String password){
        logger.info("Finding unassigned trainers");
        userService.authenticateUser(username, password);
        return trainerDao.findUnassignedTrainers();
    }

    // Activates trainer
    public void activateTrainer(String username, String password) {
        logger.info("Activating trainer with username: {}", username);
        User user = userService.authenticateUser(username, password);
        userService.setActive(user, true);
        logger.info("Trainer activated successfully");
    }

    // De-activates trainer
    public void deactivateTrainer(String username, String password) {
        logger.info("Deactivating trainer with username: {}", username);
        User user = userService.authenticateUser(username, password);
        userService.setActive(user, false);
        logger.info("Trainer deactivated successfully");
    }
}

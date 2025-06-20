package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.util.CredentialsGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    private TrainerDao trainerDao;
    private CredentialsGenerator credentialsGenerator;

    // Sets the trainer DAO through dependency injection
    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    // Sets the Credential Generator through dependency injection
    @Autowired
    public void setCredentialsGenerator(CredentialsGenerator credentialsGenerator) {
        this.credentialsGenerator = credentialsGenerator;
    }

    // Creates and saves a new trainer
    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating new trainer with ID: {}", trainer.getUserId());

        String username = credentialsGenerator.generateUsername(trainer.getFirstName(), trainer.getLastName(),
                trainerDao::usernameExists );
        String password = credentialsGenerator.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);

        return trainerDao.save(trainer);
    }

    // Gets a trainer by their ID, throws exception if not found
    public Trainer getTrainerById(String id) {
        logger.info("Getting trainer with ID: {}", id);
        try {
            return trainerDao.findById(id).orElseThrow(() -> new NotFoundException("Trainer not found"));
        } catch (NotFoundException e) {
            logger.warn("Trainer with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    // Gets a list of all trainers 
    public List<Trainer> getAllTrainers() {
        logger.info("Getting all trainers");
        return trainerDao.findAll();
    }

    // Updates an existing trainer record
    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Updating trainer with ID: {}", trainer.getUserId());
        return trainerDao.update(trainer);
    }
}

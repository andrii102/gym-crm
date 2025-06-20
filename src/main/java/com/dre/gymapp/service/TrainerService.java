package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private TrainerDao trainerDao;

    // Sets the trainer DAO through dependency injection
    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    // Creates and saves a new trainer
    public Trainer createTrainer(Trainer trainer) {
        return trainerDao.save(trainer);
    }

    // Gets a trainer by their ID, throws exception if not found
    public Trainer getTrainerById(String id) {
        return trainerDao.findById(id).orElseThrow(() -> new NotFoundException("Trainer not found"));
    }

    // Gets a list of all trainers 
    public List<Trainer> getAllTrainers() {
        return trainerDao.findAll();
    }

    // Updates an existing trainer record
    public Trainer updateTrainer(Trainer trainer) {
        return trainerDao.update(trainer);
    }
}

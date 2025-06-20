package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainingDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {

    private TrainingDao trainingDao;

    // Sets the training DAO through dependency injection
    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    // Creates and saves a new training
    public Training createTraining(Training training) {
        return trainingDao.save(training);
    }

    // Gets a training by its ID, throws exception if not found
    public Training getTrainingById(String id) {
        return trainingDao.findById(id).orElseThrow(() -> new NotFoundException("Training not found"));
    }

    // Gets a list of all trainings
    public List<Training> getAllTrainings() {
        return trainingDao.findAll();
    }
}


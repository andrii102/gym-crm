package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {

    private TraineeDao traineeDao;

    // Sets the trainee DAO through dependency injection
    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    // Creates and saves a new trainee
    public Trainee createTrainee(Trainee trainee) {
        return traineeDao.save(trainee);
    }

    // Updates an existing trainee record
    public Trainee updateTrainee(Trainee trainee) {
        return traineeDao.update(trainee);
    }

    // Gets a trainee by their ID, throws exception if not found
    public Trainee getTraineeById(String id) {
        return traineeDao.findById(id).orElseThrow(() -> new NotFoundException("Trainee not found"));
    }

    // Gets a list of all trainees
    public List<Trainee> getAllTrainees() {
        return traineeDao.findAll();
    }

    // Deletes a trainee by their ID
    public void deleteTraineeById(String id) {
        traineeDao.deleteById(id);
    }
}

package com.dre.gymapp.dao;

import com.dre.gymapp.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TraineeDao implements GenericDao<Trainee, String> {

    // Map to store trainees with their IDs as keys
    private Map<String, Trainee> traineeMap;

    @Autowired
    public void setTraineeMap(Map<String, Trainee> traineeMap) {
        this.traineeMap = traineeMap;
    }

    // Find trainee by ID, returns Optional which may or may not contain the trainee
    @Override
    public Optional<Trainee> findById(String s) {
        return Optional.ofNullable(traineeMap.get(s));
    }

    // Get a list of all trainees in the map
    @Override
    public List<Trainee> findAll() {
        return List.of(traineeMap.values().toArray(new Trainee[0]));
    }

    // Save new trainee to the map
    @Override
    public Trainee save(Trainee entity) {
        return traineeMap.put(entity.getUserId(), entity);
    }

    // Update an existing trainee if ID exists in a map
    @Override
    public Trainee update(Trainee entity) {
        if (traineeMap.containsKey(entity.getUserId())) {
            return traineeMap.put(entity.getUserId(), entity);
        }
        return null;
    }

    // Delete trainee by ID from the map
    @Override
    public void deleteById(String s) {
        traineeMap.remove(s);
    }

    // Check if a username is taken
    public boolean usernameExists(String username) {
        return traineeMap.values().stream()
                .anyMatch(trainee -> trainee.getUsername().equals(username));
    }
}
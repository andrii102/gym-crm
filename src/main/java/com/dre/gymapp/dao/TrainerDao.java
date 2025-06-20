package com.dre.gymapp.dao;

import com.dre.gymapp.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainerDao implements GenericDao<Trainer, String> {

    // Map to store trainers with their IDs as keys
    private Map<String, Trainer> trainerMap;

    @Autowired
    public void setTrainerMap(Map<String, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
    }

    // Find trainer by ID, returns Optional which may or may not contain the trainer
    @Override
    public Optional<Trainer> findById(String s) {
        return Optional.ofNullable(trainerMap.get(s));
    }

    // Get a list of all trainers in the map
    @Override
    public List<Trainer> findAll() {
        return List.of(trainerMap.values().toArray(new Trainer[0]));
    }

    // Save a new trainer to the map
    @Override
    public Trainer save(Trainer entity) {
        return trainerMap.put(entity.getUserId(), entity);
    }

    // Update the existing trainer if ID exists in map
    @Override
    public Trainer update(Trainer entity) {
        if (trainerMap.containsKey(entity.getUserId())) {
            return trainerMap.put(entity.getUserId(), entity);
        }
        return null;
    }

    // Delete operation not supported for trainers
    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException();
    }

    public boolean usernameExists(String username) {
        return trainerMap.containsKey(username);
    }

}

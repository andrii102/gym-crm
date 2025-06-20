package com.dre.gymapp.dao;

import com.dre.gymapp.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainingDao implements GenericDao<Training, String> {

    // Map to store trainings with training names as keys
    private Map<String, Training> trainingMap;

    @Autowired
    public void setTrainingMap(Map<String, Training> trainingMap) {
        this.trainingMap = trainingMap;
    }

    // Find training by name, returns Optional which may or may not contain the training
    @Override
    public Optional<Training> findById(String s) {
        return Optional.ofNullable(trainingMap.get(s));
    }

    // Get a list of all trainings in the map
    @Override
    public List<Training> findAll() {
        return List.of(trainingMap.values().toArray(new Training[0]));
    }

    // Save new training to the map using training name as key
    @Override
    public Training save(Training entity) {
        return trainingMap.put(entity.getTrainingName(), entity);
    }

    // Update operation isn't supported for trainings
    @Override
    public Training update(Training entity) {
        throw new UnsupportedOperationException();
    }

    // Delete operation not supported for trainings
    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException();
    }
}

package com.dre.gymapp.dao;

import com.dre.gymapp.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainingDao implements GenericDao<Training, String> {

    @Autowired
    private Map<String, Training> trainingMap;

    @Override
    public Optional<Training> findById(String s) {
        return Optional.ofNullable(trainingMap.get(s));
    }

    @Override
    public List<Training> findAll() {
        return List.of(trainingMap.values().toArray(new Training[0]));
    }

    @Override
    public Training save(Training entity) {
        return trainingMap.put(entity.getTrainingName(), entity);
    }

    @Override
    public Training update(Training entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException();
    }
}

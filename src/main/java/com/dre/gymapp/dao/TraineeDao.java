package com.dre.gymapp.dao;

import com.dre.gymapp.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TraineeDao implements GenericDao<Trainee, String>{

    @Autowired
    private Map<String, Trainee> traineeMap;

    @Override
    public Optional<Trainee> findById(String s) {
        return Optional.ofNullable(traineeMap.get(s));
    }

    @Override
    public List<Trainee> findAll() {
        return List.of(traineeMap.values().toArray(new Trainee[0]));
    }

    @Override
    public Trainee save(Trainee entity) {
        return traineeMap.put(entity.getUserId(), entity);
    }

    @Override
    public Trainee update(Trainee entity) {
        if(traineeMap.containsKey(entity.getUserId())){
            return traineeMap.put(entity.getUserId(), entity);
        }
        return null;
    }

    @Override
    public void deleteById(String s) {
        traineeMap.remove(s);
    }
}

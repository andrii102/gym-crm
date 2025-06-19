package com.dre.gymapp.dao;

import com.dre.gymapp.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainerDao implements GenericDao<Trainer, String>{

    @Autowired
    private Map<String, Trainer> trainerMap;

    @Override
    public Optional<Trainer> findById(String s) {
        return Optional.ofNullable(trainerMap.get(s));
    }

    @Override
    public List<Trainer> findAll() {
        return List.of(trainerMap.values().toArray(new Trainer[0]));
    }

    @Override
    public Trainer save(Trainer entity) {
        return trainerMap.put(entity.getUserId(), entity);
    }

    @Override
    public Trainer update(Trainer entity) {
        if(trainerMap.containsKey(entity.getUserId())){
            return trainerMap.put(entity.getUserId(), entity);
        }
        return null;
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException();
    }
}

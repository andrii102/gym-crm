package com.dre.gymapp.dao;

import com.dre.gymapp.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingTypeDao {

    @PersistenceContext
    private EntityManager entityManager;

    public TrainingType findById(Long id) {
        return entityManager.find(TrainingType.class, id);
    }

    public List<TrainingType> findAll() {
        return entityManager.createQuery("select t from TrainingType t", TrainingType.class).getResultList();
    }
}

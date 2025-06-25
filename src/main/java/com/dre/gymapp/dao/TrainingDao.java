package com.dre.gymapp.dao;

import com.dre.gymapp.model.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDao implements GenericDao<Training, Long> {

    // EntityManager for handling persistence operations
    @PersistenceContext
    private EntityManager entityManager;

    // Finds a training by ID in the database
    @Override
    public Optional<Training> findById(Long aLong) {
        return Optional.of(entityManager.find(Training.class, aLong));
    }

    // Retrieves all trainings from the database using criteria API
    @Override
    public List<Training> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        return entityManager.createQuery(query.select(root)).getResultList();
    }

    // Persists a new training entity to the database
    @Override
    @Transactional
    public void save(Training entity) {
        entityManager.persist(entity);
    }

    // Updates are not supported for training entities
    @Override
    public Training update(Training entity) {
        throw new UnsupportedOperationException();
    }

    // Deleting trainings is not supported 
    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException();
    }
}
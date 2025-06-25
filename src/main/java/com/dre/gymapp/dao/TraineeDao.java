package com.dre.gymapp.dao;

import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDao implements GenericDao<Trainee, Long> {

    // Entity manager for database operations
    @PersistenceContext
    private EntityManager entityManager;

    // Find trainee by ID, returns Optional which may or may not contain the trainee
    @Override
    public Optional<Trainee> findById(Long aLong) {
        return Optional.of(entityManager.find(Trainee.class, aLong));
    }

    // Get a list of all trainees
    @Override
    public List<Trainee> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainee> query = cb.createQuery(Trainee.class);
        Root<Trainee> root = query.from(Trainee.class);

        TypedQuery<Trainee> typedQuery = entityManager.createQuery(query.select(root));
        return typedQuery.getResultList();
    }

    // Save a new trainee
    @Override
    @Transactional
    public void save(Trainee entity) {
        entityManager.persist(entity);
    }

    // Update an existing trainee if it exists in a table
    @Override
    @Transactional
    public Trainee update(Trainee entity) {
        if (entityManager.find(Trainee.class, entity.getId()) != null) {
            return entityManager.merge(entity);
        }
        throw new NotFoundException("Trainee with ID " + entity.getId() + " not found");
    }

    // Delete trainee by ID from the table
    @Override
    @Transactional
    public void deleteById(Long aLong) {
        Trainee trainee = entityManager.find(Trainee.class, aLong);
        if (trainee == null) {
            throw new NotFoundException("Trainee with ID " + aLong + " not found");
        }
        entityManager.remove(trainee);
    }

    // Delete trainee by username from the table
    @Transactional
    public void deleteTraineeByUsername(String username){
        Trainee trainee = entityManager.find(Trainee.class, username);
        if (trainee == null) {
            throw new NotFoundException("Trainee with username " + username + " not found");
        }
    }

}
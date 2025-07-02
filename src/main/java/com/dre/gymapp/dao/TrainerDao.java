package com.dre.gymapp.dao;

import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainer;
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
public class TrainerDao {

    // EntityManager for handling persistence operations
    @PersistenceContext
    private EntityManager entityManager;

    // Finds a trainer by their ID in the database
    public Optional<Trainer> findById(Long aLong) {
        return Optional.ofNullable(entityManager.find(Trainer.class, aLong));
    }

    // Retrieves all trainers from the database
    public List<Trainer> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainer> query = cb.createQuery(Trainer.class);
        Root<Trainer> root = query.from(Trainer.class);

        return entityManager.createQuery(query.select(root)).getResultList();
    }

    // Persists a new trainer entity to the database
    @Transactional
    public void save(Trainer entity) {
        entityManager.persist(entity);
    }

    // Updates an existing trainer in the database if found
    @Transactional
    public Trainer update(Trainer entity) {
        if (entityManager.find(Trainer.class, entity.getId()) != null) {
            return entityManager.merge(entity);
        }
        throw new NotFoundException("Trainer with ID " + entity.getId() + " not found");
    }

    // Returns list of unassigned trainers
    public List<Trainer> findUnassignedTrainers() {
        String sql = "SELECT * FROM trainer WHERE user_id NOT IN (SELECT trainer_id FROM trainee_trainer)";
        List<?> rawList = entityManager.createNativeQuery(sql, Trainer.class).getResultList();
        return (List<Trainer>) rawList;
    }

}
package com.dre.gymapp.dao;

import com.dre.gymapp.model.Training;
import com.dre.gymapp.model.TrainingStatus;
import com.dre.gymapp.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDao {

    // EntityManager for handling persistence operations
    @PersistenceContext
    private EntityManager entityManager;

    // Finds a training by ID in the database
    public Optional<Training> findById(Long aLong) {
        return Optional.ofNullable(entityManager.find(Training.class, aLong));
    }

    // Retrieves all trainings from the database using criteria API
    public List<Training> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        return entityManager.createQuery(query.select(root)).getResultList();
    }

    // Persists a new training entity to the database
    @Transactional
    public void save(Training entity) {
        entityManager.persist(entity);
    }

    // Finds trainings by parameters
    public List<Training> findTrainingsByParams(String trainerUsername, String traineeUsername,
                                                LocalDateTime fromDateTime, LocalDateTime toDateTime, String trainingTypeName, TrainingStatus trainingStatus, Integer limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        if (trainerUsername != null && !trainerUsername.isEmpty()) {
            predicates.add(cb.equal(root.get("trainer").get("user").get("username"), trainerUsername));
        }
        if (traineeUsername != null && !traineeUsername.isEmpty()) {
            predicates.add(cb.equal(root.get("trainee").get("user").get("username"), traineeUsername));
        }
        if (fromDateTime != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDateTime"), fromDateTime));
        }
        if (toDateTime != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("trainingDateTime"), toDateTime));
        }
        if (trainingStatus != null) {
            predicates.add(cb.equal(root.get("status"), trainingStatus));
        }
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType");
            predicates.add(cb.equal(cb.lower(trainingTypeJoin.get("trainingTypeName")), trainingTypeName.toLowerCase()));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Training> typedQuery = entityManager.createQuery(query);

        if (limit != null && limit > 0) {
            typedQuery.setMaxResults(limit);
        }

        return typedQuery.getResultList();
    }

    @Transactional
    public void update(Training training) {
        if (training == null || training.getId() == null) {
            throw new IllegalArgumentException("Training ID cannot be null for update");
        }
        entityManager.merge(training);
    }
}
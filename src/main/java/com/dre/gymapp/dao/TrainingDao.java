package com.dre.gymapp.dao;

import com.dre.gymapp.model.Training;
import com.dre.gymapp.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
                                                LocalDate fromDate, LocalDate toDate, String trainingTypeName) {
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
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("trainingDate"), toDate));
        }
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType");
            predicates.add(cb.equal(cb.lower(trainingTypeJoin.get("trainingTypeName")), trainingTypeName.toLowerCase()));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
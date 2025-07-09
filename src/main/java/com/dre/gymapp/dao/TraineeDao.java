package com.dre.gymapp.dao;

import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDao {

    // Entity manager for database operations
    @PersistenceContext
    private EntityManager entityManager;

    // Find trainee by ID, returns Optional which may or may not contain the trainee
    public Optional<Trainee> findById(Long aLong) {
        return Optional.ofNullable(entityManager.find(Trainee.class, aLong));
    }

    // Get a list of all trainees
    public List<Trainee> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainee> query = cb.createQuery(Trainee.class);
        Root<Trainee> root = query.from(Trainee.class);

        TypedQuery<Trainee> typedQuery = entityManager.createQuery(query.select(root));
        return typedQuery.getResultList();
    }

    // Save a new trainee
    @Transactional
    public void save(Trainee entity) {
        entityManager.persist(entity);
    }

    // Update an existing trainee if it exists in a table
    @Transactional
    public Trainee update(Trainee entity) {
        if (entity.getId() == null || entityManager.find(Trainee.class, entity.getId()) == null) {
            throw new NotFoundException("Trainee with ID " + entity.getId() + " not found");
        }
        return entityManager.merge(entity);
    }


    // Delete trainee by ID from the table
    @Transactional
    public void deleteById(Long aLong) {
        Trainee trainee = entityManager.find(Trainee.class, aLong);
        if (trainee == null) {
            throw new NotFoundException("Trainee with ID " + aLong + " not found");
        }
        entityManager.remove(trainee);
    }

    @Transactional
    public void delete(Trainee trainee) {
        entityManager.remove(entityManager.contains(trainee) ? trainee : entityManager.merge(trainee));
    }

    public Optional<Trainee> findByUserId(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainee> query = cb.createQuery(Trainee.class);
        Root<Trainee> root = query.from(Trainee.class);
        query.select(root).where(cb.equal(root.get("user").get("id"), id));
        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }

    public Trainee findByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainee> query = cb.createQuery(Trainee.class);
        Root<Trainee> root = query.from(Trainee.class);
        Join<Trainee, User> userJoin = root.join("user");
        query.select(root).where((cb.equal(userJoin.get("username"), username)));
        return entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
    }
}
package com.dre.gymapp.dao;

import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
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

    // Finds trainers by usernames from the list
    public List<Trainer> findByUsernames(List<String> usernames) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainer> query = cb.createQuery(Trainer.class);
        Root<Trainer> root = query.from(Trainer.class);
        Join<Trainer, User> userJoin = root.join("user");

        query.select(root).where(userJoin.get("username").in(usernames));

        return entityManager.createQuery(query).getResultList();
    }

    public Optional<Trainer> findByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainer> query = cb.createQuery(Trainer.class);
        Root<Trainer> root = query.from(Trainer.class);
        Join<Trainer, User> userJoin = root.join("user");
        query.select(root).where(cb.equal(userJoin.get("username"), username));
        return entityManager.createQuery(query).getResultList().stream().findFirst();
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
    public List<Trainer> findUnassignedTrainersOnTrainee(Long traineeId) {
        String sql = "SELECT * FROM trainer WHERE user_id NOT IN " +
                "(SELECT trainer_id FROM trainee_trainer WHERE trainee_id = :traineeId)";
        List<?> rawList = entityManager.createNativeQuery(sql, Trainer.class)
                .setParameter("traineeId", traineeId)
                .getResultList();
        return (List<Trainer>) rawList;
    }

}
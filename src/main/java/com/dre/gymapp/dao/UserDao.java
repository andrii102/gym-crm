package com.dre.gymapp.dao;

import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.User;
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
public class UserDao implements GenericDao<User, Long> {
    // EntityManager for handling persistence operations
    @PersistenceContext
    private EntityManager entityManager;


    // Finds a user by their ID in the database
    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.ofNullable(entityManager.find(User.class, aLong));
    }

    // Retrieves all users from the database
    @Override
    public List<User> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        return entityManager.createQuery(query.select(root)).getResultList();
    }

    // Persists a new user entity to the database
    @Override
    @Transactional
    public void save(User entity) {
        entityManager.persist(entity);
    }

    // Updates an existing user in the database if found
    @Override
    @Transactional
    public User update(User entity) {
        if (entity.getId() == null) {
         throw new IllegalArgumentException("User ID cannot be null for update");
        }
        return entityManager.merge(entity);
    }

    // Deletes a user by their ID from the database
    @Override
    @Transactional
    public void deleteById(Long aLong) {
        User user = entityManager.find(User.class, aLong);
        if (user == null) {
            throw new NotFoundException("User with ID " + aLong + " not found");
        }
        entityManager.remove(user);
    }

    // Find usernames that start with the given prefix
    public List<String> findByUsernameStartingWith(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<User> root = query.from(User.class);
        query.select(root.get("username")).where(cb.like(root.get("username"), username + "%"));

        return entityManager.createQuery(query).getResultList();
    }

    // Finds a user by their username in the database
    public Optional<User> findByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        return entityManager.createQuery(query.select(root)
                        .where(cb.equal(root.get("username"), username)))
                .getResultList().stream().findFirst();
    }
}
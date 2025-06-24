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
public class UserDao implements GenericDao<User, Long>{
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.of(entityManager.find(User.class, aLong));
    }

    @Override
    public List<User> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        return entityManager.createQuery(query.select(root)).getResultList();
    }

    @Override
    @Transactional
    public void save(User entity) {
        entityManager.persist(entity);
    }

    @Override
    public User update(User entity) {
        User user = entityManager.find(User.class, entity.getId());
        if (user != null) {
            return entityManager.merge(entity);
        }
        throw  new NotFoundException("User with ID " + entity.getId() + " not found");
    }

    @Override
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
}

package com.dre.gymapp.dao;

import com.dre.gymapp.model.Trainee;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID>{
    Optional<T> findById(ID id);
    List<T> findAll();
    void save(T entity);
    T update(T entity);
    void deleteById(ID id);
}

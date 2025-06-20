package com.dre.gymapp.dao;

import com.dre.gymapp.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDaoTest {

    private TraineeDao traineeDao;
    private Map<String, Trainee> traineeMap;

    @BeforeEach
    void setUp() {
        traineeMap = new HashMap<>();
        traineeDao = new TraineeDao();
        traineeDao.setTraineeMap(traineeMap);
    }

    @Test
    void saveTest() {
        Trainee trainee = new Trainee("T1", "John", "Doe");
        traineeDao.save(trainee);
        
        assertEquals(trainee, traineeMap.get("T1"));
    }

    @Test
    void findByIdTest() {
        Trainee trainee = new Trainee("T2", "Alice", "Smith");
        traineeMap.put("T2", trainee);

        Optional<Trainee> result = traineeDao.findById("T2");

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
    }

    @Test
    void findAllTest() {
        Trainee t1 = new Trainee("T3", "Tom", "Brown");
        Trainee t2 = new Trainee("T4", "Sara", "Jones");
        traineeMap.put("T3", t1);
        traineeMap.put("T4", t2);

        List<Trainee> all = traineeDao.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(t1));
        assertTrue(all.contains(t2));
    }

    @Test
    void updateExistingTraineeTest() {
        Trainee old = new Trainee("T5", "Bob", "White");
        traineeMap.put("T5", old);

        Trainee updated = new Trainee("T5", "Bob", "Black");
        Trainee result = traineeDao.update(updated);

        assertEquals(old, result); // returns previous value
        assertEquals("Black", traineeMap.get("T5").getLastName());
    }

    @Test
    void updateNonExistingTraineeTest() {
        Trainee newTrainee = new Trainee("T6", "New", "Guy");

        Trainee result = traineeDao.update(newTrainee);

        assertNull(result);
        assertFalse(traineeMap.containsKey("T6"));
    }

    @Test
    void deleteByIdTest() {
        Trainee t = new Trainee("T7", "Delete", "Me");
        traineeMap.put("T7", t);

        traineeDao.deleteById("T7");

        assertFalse(traineeMap.containsKey("T7"));
    }

    @Test
    void usernameExistsTrueTest() {
        Trainee t = new Trainee("T8", "User", "Name");
        t.setUsername("User.Name");
        traineeMap.put("T8", t);

        assertTrue(traineeDao.usernameExists("User.Name"));
    }

    @Test
    void usernameExistsFalseTest() {
        assertFalse(traineeDao.usernameExists("Ghost.User"));
    }
}

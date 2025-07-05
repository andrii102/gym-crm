package com.dre.gymapp.dao;

import com.dre.gymapp.config.H2TestConfig;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2TestConfig.class)
@Transactional
class TraineeDaoTest {

    @Autowired
    private TraineeDao traineeDao;

    @PersistenceContext
    private EntityManager entityManager;

    private User testUser;
    private Trainee testTrainee;
    private static final String TEST_USERNAME = "test.user";
    private static final String TEST_ADDRESS = "123 Main St";
    private static final Long NON_EXISTENT_ID = 9999L;

    @BeforeEach
    void setUp() {
        testUser = new User("Test", "User");
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword("<PASSWORD>");
        entityManager.persist(testUser);

        testTrainee = new Trainee(LocalDate.of(2025, 1, 1), TEST_ADDRESS);
        testTrainee.setUser(testUser);
        entityManager.persist(testTrainee);

        entityManager.flush();
    }

    @Test
    void save_ShouldPersistNewTrainee() {
        User user = new User("Test", "User");
        user.setUsername("new.user1");
        user.setPassword("<PASSWORD>");
        entityManager.persist(user);

        Trainee trainee = new Trainee(LocalDate.of(2025, 1, 1), TEST_ADDRESS);
        trainee.setUser(user);

        traineeDao.save(trainee);
        entityManager.flush();
        entityManager.clear();

        Trainee persisted = entityManager.find(Trainee.class, trainee.getId());

        assertAll(
                () -> assertNotNull(persisted),
                () -> assertEquals(TEST_ADDRESS, persisted.getAddress())

        );
    }

    @Test
    void findById_ShouldReturnTrainee() {
        Optional<Trainee> trainee = traineeDao.findById(testTrainee.getId());

        assertTrue(trainee.isPresent());
        assertEquals("123 Main St", trainee.get().getAddress());
        assertEquals(TEST_USERNAME, trainee.get().getUser().getUsername());
    }

    @Test
    public void findById_ShouldReturnEmptyOptional() {
        Optional<Trainee> trainee = traineeDao.findById(NON_EXISTENT_ID);
        assertFalse(trainee.isPresent());
    }

    @Test
    void findAll_ShouldReturnTrainees() {
        List<Trainee> trainees = traineeDao.findAll();
        assertFalse(trainees.isEmpty());
    }

    @Test
    void update_ShouldUpdateExistingTrainee() {
        Trainee trainee = traineeDao.findById(testTrainee.getId()).orElseThrow();

        String newAddress = "456 New St";
        trainee.setAddress(newAddress);

        traineeDao.update(trainee);
        entityManager.flush();
        entityManager.clear();

        Trainee dbTrainee = entityManager.find(Trainee.class, testTrainee.getId());
        assertEquals(newAddress, dbTrainee.getAddress());

    }

    @Test
    void update_ShouldThrowForNewTrainee() {
        Trainee trainee = new Trainee(LocalDate.of(2025, 1, 1), TEST_ADDRESS);
        trainee.setUser(testUser);

        assertThrows(NotFoundException.class, () -> traineeDao.update(trainee));
    }

    @Test
    void deleteById_ShouldRemoveTrainee() {
        traineeDao.deleteById(testTrainee.getId());
        assertFalse(traineeDao.findById(testTrainee.getId()).isPresent());
    }

    @Test
    public void deleteById_ShouldThrowForNonExistentTrainee() {
        assertThrows(NotFoundException.class, () -> traineeDao.deleteById(NON_EXISTENT_ID));
    }

}
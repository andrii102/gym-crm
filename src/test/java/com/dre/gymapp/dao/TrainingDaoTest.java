package com.dre.gymapp.dao;

import com.dre.gymapp.config.H2TestConfig;
import com.dre.gymapp.model.*;
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
public class TrainingDaoTest {

    @Autowired
    private TrainingDao trainingDao;

    @PersistenceContext
    private EntityManager entityManager;

    private User testUserTrainee;
    private User testUserTrainer;
    private Trainer testTrainer;
    private TrainingType testTrainingType;
    private Trainee testTrainee;
    private Training testTraining;
    private static final String TEST_TRAINEE_USERNAME = "trainee.user";
    private static final String TEST_TRAINER_USERNAME = "trainer.user";
    private static final String TEST_TRAINING_NAME = "Test Training";
    private static final String TRAINING_TYPE = "CARDIO";
    private static final LocalDate TEST_DATE = LocalDate.of(2025, 1, 1);
    private static final Integer TEST_DURATION = 60;
    private static final Long NON_EXISTENT_ID = 9999L;

    @BeforeEach
    public void setUp(){
        testUserTrainee = new User("Trainee", "User");
        testUserTrainee.setUsername(TEST_TRAINEE_USERNAME);
        testUserTrainee.setPassword("<PASSWORD>");
        entityManager.persist(testUserTrainee);

        testUserTrainer = new User("Trainer", "User");
        testUserTrainer.setUsername(TEST_TRAINER_USERNAME);
        testUserTrainer.setPassword("<PASSWORD>");
        entityManager.persist(testUserTrainer);

        testTrainingType = new TrainingType(TRAINING_TYPE);
        entityManager.persist(testTrainingType);

        testTrainer = new Trainer(testTrainingType, testUserTrainer);
        entityManager.persist(testTrainer);

        testTrainee = new Trainee(null, null);
        testTrainee.setUser(testUserTrainee);
        entityManager.persist(testTrainee);

        testTraining = new Training(testTrainee, testTrainer, TEST_TRAINING_NAME,
                testTrainingType, TEST_DATE, TEST_DURATION);
        entityManager.persist(testTraining);

        entityManager.flush();
    }

    @Test
    public void findById_ShouldReturnTraining() {
        Optional<Training> training = trainingDao.findById(testTraining.getId());
        assertTrue(training.isPresent());
        assertEquals(TRAINING_TYPE, training.get().getTrainingType().getTrainingTypeName());
    }

    @Test
    public void findById_ShouldReturnEmptyOptional() {
        Optional<Training> training = trainingDao.findById(NON_EXISTENT_ID);
        assertFalse(training.isPresent());
    }

    @Test
    public void findAll_ShouldReturnTrainings() {
        List<Training> trainings = trainingDao.findAll();
        assertFalse(trainings.isEmpty());
    }

    @Test
    public void save_ShouldPersistNewTraining() {
        Training training = new Training(testTrainee, testTrainer, "Save Test Training",
                testTrainingType, TEST_DATE, TEST_DURATION);
        trainingDao.save(training);
        entityManager.flush();
        entityManager.clear();

        Training persisted = entityManager.find(Training.class, training.getId());

        assertAll(
                () -> assertNotNull(persisted),
                () -> assertEquals("Save Test Training", persisted.getTrainingName()),
                () -> assertEquals(TRAINING_TYPE, persisted.getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    public void findTrainingsByParams_WithAllParams_ShouldReturnList(){
        List<Training> trainings = trainingDao.findTrainingsByParams(testUserTrainer.getUsername(), testUserTrainee.getUsername(),
                TEST_DATE.minusDays(7), TEST_DATE.plusDays(7), TRAINING_TYPE);

        assertFalse(trainings.isEmpty());
        assertEquals(1, trainings.size());
        assertEquals(TEST_TRAINING_NAME, trainings.getFirst().getTrainingName());
    }

    @Test
    public void findTrainingsByParams_WithNoParams_ShouldReturnList(){
        List<Training> trainings = trainingDao.findTrainingsByParams(null, null, null, null, null);

        assertFalse(trainings.isEmpty());
    }

    @Test
    public void findTrainingsByParams_WithNoMatchingParams_ShouldReturnEmptyList(){
        List<Training> trainings = trainingDao.findTrainingsByParams("nonexisting.user", "nonexisting.user",
                TEST_DATE.minusDays(7), TEST_DATE.plusDays(7), TRAINING_TYPE);

        assertTrue(trainings.isEmpty());
    }

}

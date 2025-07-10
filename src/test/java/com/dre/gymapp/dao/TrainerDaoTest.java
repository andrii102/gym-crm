package com.dre.gymapp.dao;

import com.dre.gymapp.config.H2TestConfig;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.TrainingType;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2TestConfig.class)
@Transactional
public class TrainerDaoTest {

    @Autowired
    private TrainerDao trainerDao;

    @PersistenceContext
    private EntityManager entityManager;

    private User testUser;
    private Trainer testTrainer;
    private TrainingType testTrainingType;
    private static final String TEST_USERNAME = "test.user";
    private static final String TRAINING_TYPE = "CARDIO";
    private static final Long NON_EXISTENT_ID = 9999L;

    @BeforeEach
    public void setUp(){
        testUser = new User("Test", "User");
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword("<PASSWORD>");
        entityManager.persist(testUser);

        testTrainingType = new TrainingType(TRAINING_TYPE);
        entityManager.persist(testTrainingType);
        testTrainer = new Trainer(testTrainingType, testUser);
        entityManager.persist(testTrainer);

        entityManager.flush();
    }

    @Test
    public void findById_ShouldReturnTrainer() {
        Optional<Trainer> trainer = trainerDao.findById(testTrainer.getId());
        assertTrue(trainer.isPresent());
        assertEquals(TRAINING_TYPE, trainer.get().getSpecialization().getTrainingTypeName());
    }

    @Test
    public void findById_ShouldReturnEmptyOptional() {
        Optional<Trainer> trainer = trainerDao.findById(NON_EXISTENT_ID);
        assertFalse(trainer.isPresent());
    }

    @Test
    public void findAll_ShouldReturnTrainers() {
        List<Trainer> trainers = trainerDao.findAll();
        assertFalse(trainers.isEmpty());
    }

    @Test
    public void save_ShouldPersistNewTrainer() {
        User user = new User("Test", "User");
        user.setUsername("new.user1");
        user.setPassword("<PASSWORD>");
        Trainer trainer = new Trainer(testTrainingType, user);

        entityManager.persist(user);

        trainerDao.save(trainer);
        entityManager.flush();
        entityManager.clear();

        Trainer persisted = entityManager.find(Trainer.class, trainer.getId());

        assertAll(
                () -> assertNotNull(persisted),
                () -> assertEquals(TRAINING_TYPE, persisted.getSpecialization().getTrainingTypeName())
        );
    }


    @Test
    public void update_ShouldUpdateExistingTrainer() {
        Trainer trainer = trainerDao.findById(testTrainer.getId()).orElseThrow();

        String newTrainingType = "BASKETBALL";
        TrainingType trainingType = new TrainingType(newTrainingType);
        entityManager.persist(trainingType);
        trainer.setSpecialization(trainingType);

        trainerDao.update(trainer);
        entityManager.flush();
        entityManager.clear();

        Trainer dbTrainer = entityManager.find(Trainer.class, testTrainer.getId());
        assertEquals(newTrainingType, dbTrainer.getSpecialization().getTrainingTypeName());
    }


    @Test
    public void update_ShouldThrowForNewTrainer() {
        Trainer trainer = new Trainer(testTrainingType, testUser);
        trainer.setId(NON_EXISTENT_ID);
        assertThrows(NotFoundException.class, () -> trainerDao.update(trainer));
    }

    @Test
    public void findUnassignedTrainers_ShouldReturnTrainers() {
        User user = new User("Test", "User");
        user.setUsername("new.user1");
        user.setPassword("<PASSWORD>");
        entityManager.persist(user);

        Trainee trainee = new Trainee(null, null);
        trainee.setUser(user);
        trainee.setTrainers(List.of(testTrainer));
        entityManager.persist(trainee);
        entityManager.flush();

        List<Trainer> trainers = trainerDao.findUnassignedTrainersOnTrainee(trainee.getId());
        System.out.println(trainers.size());
        assertFalse(trainers.isEmpty());
        assertEquals(3, trainers.size());
    }

}

package com.dre.gymapp.dao;

import com.dre.gymapp.model.Training;
import com.dre.gymapp.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDaoTest {

    private TrainingDao trainingDao;
    private Map<String, Training> trainingMap;

    @BeforeEach
    void setUp() {
        trainingMap = new HashMap<>();
        trainingDao = new TrainingDao();
        trainingDao.setTrainingMap(trainingMap);
    }

    private Training createSampleTraining(String name) {
        return new Training(
                "Trainee01",
                "Trainer01",
                name,
                TrainingType.CARDIO,
                LocalDate.of(2025, 6, 20),
                60
        );
    }

    @Test
    void save_ShouldAddTraining() {
        Training training = createSampleTraining("Workout1");

        Training result = trainingDao.save(training);

        assertNull(result);
        assertEquals(training, trainingMap.get("Workout1"));
    }

    @Test
    void findById_ShouldReturnTrainingIfExists() {
        Training training = createSampleTraining("Cardio");
        trainingMap.put("Cardio", training);

        Optional<Training> result = trainingDao.findById("Cardio");

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void findById_ShouldReturnEmptyIfNotExists() {
        Optional<Training> result = trainingDao.findById("NonExistent");

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllTrainings() {
        Training t1 = createSampleTraining("Yoga");
        Training t2 = createSampleTraining("HIIT");

        trainingMap.put("Yoga", t1);
        trainingMap.put("HIIT", t2);

        List<Training> all = trainingDao.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(t1));
        assertTrue(all.contains(t2));
    }

    @Test
    void update_ShouldThrowUnsupportedOperationException() {
        Training training = createSampleTraining("Boxing");

        assertThrows(UnsupportedOperationException.class, () -> trainingDao.update(training));
    }

    @Test
    void deleteById_ShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> trainingDao.deleteById("Boxing"));
    }
}

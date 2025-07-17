package com.dre.gymapp.dao;

import com.dre.gymapp.config.H2TestConfig;
import com.dre.gymapp.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2TestConfig.class)
@Transactional
public class TrainingTypeDaoTest {

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Test
    public void findById_ShouldReturnEntity() {
        TrainingType trainingType = trainingTypeDao.findById(1L);

        assertNotNull(trainingType);
        assertEquals("Yoga", trainingType.getTrainingTypeName());
    }

    @Test
    public void findById_ShouldReturnNull() {
        TrainingType trainingType = trainingTypeDao.findById(9999L);

        assertNull(trainingType);
    }

    @Test
    public void findAll_ShouldReturnEntities() {
        List<TrainingType> trainingTypes = trainingTypeDao.findAll();

        assertNotNull(trainingTypes);
        assertEquals(3, trainingTypes.size());
    }
}

package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainingDao;
import com.dre.gymapp.dto.trainings.NewTrainingRequest;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @InjectMocks
    private TrainingService trainingService;
    @Mock
    private TrainingDao trainingDao;

    private Training testTraining;

    @BeforeEach
    void setUp() {
        testTraining = new Training();
        testTraining.setTrainingName("Morning Yoga");
    }

    @Test
    public void createTraining_ShouldCreateTrainingRecord(){
        Training result = trainingService.createTraining(new NewTrainingRequest("trainee.user", "trainer.user",
                testTraining.getTrainingName(), null, null));

        assertNotNull(result);
        assertEquals(testTraining.getTrainingName(), result.getTrainingName());
        verify(trainingDao).save(testTraining);
    }

    @Test
    public void getTrainingBtId(){
        when(trainingDao.findById(any())).thenReturn(Optional.of(testTraining));
        Training result = trainingService.getTrainingById(testTraining.getId());

        assertNotNull(result);
        assertEquals(testTraining.getId(), result.getId());
        assertEquals(testTraining.getTrainingName(), result.getTrainingName());

        verify(trainingDao).findById(any());
    }

    @Test
    public void getTrainingBtId_ShouldThrowException_TrainingNotFound(){
        when(trainingDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> trainingService.getTrainingById(1L));
    }

    @Test
    public void getAllTrainings(){
        when(trainingDao.findAll()).thenReturn(List.of(testTraining, testTraining));
        List<Training> trainings = trainingService.getAllTrainings();

        assertNotNull(trainings);
        assertEquals(2, trainings.size());
    }

    @Test
    public void getTrainerTrainings_ShouldReturnTrainingsByParams(){
        List<Training> trainings = List.of(testTraining, testTraining);

        when(trainingDao.findTrainingsByParams(any(),any(),any(),any(),any())).thenReturn(trainings);

        List<Training> result = trainingService.getTrainingsByParams("trainer", "trainee",
                null, null, "first training");

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(trainingDao).findTrainingsByParams(any(),any(),any(),any(),any());
    }

}

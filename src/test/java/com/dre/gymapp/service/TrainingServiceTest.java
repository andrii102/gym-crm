package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.dao.TrainingDao;
import com.dre.gymapp.dao.TrainingTypeDao;
import com.dre.gymapp.dto.trainings.NewTrainingRequest;
import com.dre.gymapp.dto.trainings.TrainingEventRequest;
import com.dre.gymapp.dto.trainings.TrainingTypeResponse;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @InjectMocks
    private TrainingService trainingService;
    @Mock
    private TrainingDao trainingDao;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private TrainingTypeDao trainingTypeDao;
    @Mock
    private TrainingProducer trainingProducer;

    private Training testTraining;

    @BeforeEach
    void setUp() {
        testTraining = new Training();
        testTraining.setTrainingName("Morning Yoga");
    }

    @Test
    public void createTraining_ShouldCreateTrainingRecord(){
        User traineeUser = new User("Trainee", "User");
        User trainerUser = new User("Trainer", "User");
        traineeUser.setUsername("trainee.user");
        trainerUser.setUsername("trainer.user");

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        Trainer trainer = new Trainer(new TrainingType("RUNNING"), trainerUser);
        trainer.setUser(trainerUser);

        NewTrainingRequest request = new NewTrainingRequest("trainee.user", "trainer.user",
                testTraining.getTrainingName(), LocalDateTime.of(2025, 1, 1, 1, 1), null, TrainingStatus.SCHEDULED);

        when(traineeDao.findByUsername(any())).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername(any())).thenReturn(Optional.of(trainer));

        Training result = trainingService.createTraining(request);

        assertNotNull(result);
        assertEquals(testTraining.getTrainingName(), result.getTrainingName());
        verify(trainingDao).save(any());
        verify(trainingProducer).send(eq("trainings.queue"), any(TrainingEventRequest.class));
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

        when(trainingDao.findTrainingsByParams(any(),any(),any(),any(),any(), any(), any())).thenReturn(trainings);

        List<Training> result = trainingService.getTrainingsByParams("trainer", "trainee",
                null, null, "first training", null, null);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(trainingDao).findTrainingsByParams(any(),any(),any(),any(),any(), any(), any());
    }

    @Test
    void getAllTrainingTypes_ReturnsMappedDtos() {
        TrainingType type1 = new TrainingType();
        TrainingType type2 = new TrainingType();
        List<TrainingType> trainingTypeList = List.of(type1, type2);

        when(trainingTypeDao.findAll()).thenReturn(trainingTypeList);

        List<TrainingTypeResponse> result = trainingService.getAllTrainingTypes();

        assertEquals(2, result.size());
    }

}

package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainingDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Training;
import com.dre.gymapp.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @InjectMocks
    private TrainingService trainingService;
    @Mock
    private TrainingDao trainingDao;

    @Test
    public void testCreateTraining() {
        Training inputTraining = new Training("T0", "TR0", "Test",
                TrainingType.BALANCE, null, null);
        Training expectedTraining = new Training("T0", "TR0", "Test",
                TrainingType.BALANCE, null, null);

        when(trainingDao.save(inputTraining)).thenReturn(expectedTraining);

        Training result = trainingService.createTraining(inputTraining);

        verify(trainingDao).save(inputTraining);
        assertEquals(result.getTraineeId(), expectedTraining.getTraineeId());
        assertEquals(result.getTrainingName(), expectedTraining.getTrainingName());
        assertEquals(result.getTrainingType(), expectedTraining.getTrainingType());
        assertEquals(result.getTrainingDate(), expectedTraining.getTrainingDate());
    }

    @Test
    public void testGetTrainingByIdExistingTraining() {
        String id = "T0";
        Training expectedTraining = new Training(id, "TR0", "Test",
                TrainingType.BALANCE, null, null);

        when(trainingDao.findById(id)).thenReturn(Optional.of(expectedTraining));

        Training result = trainingService.getTrainingById(id);

        verify(trainingDao).findById(id);
        assertEquals(result.getTraineeId(), expectedTraining.getTraineeId());
        assertEquals(result.getTrainingName(), expectedTraining.getTrainingName());
        assertEquals(result.getTrainingType(), expectedTraining.getTrainingType());
        assertEquals(result.getTrainingDate(), expectedTraining.getTrainingDate());
    }

    @Test
    public void testGetTrainingByIdNonExistingTraining() {
        String id = "T0";
        when(trainingDao.findById(id)).thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> trainingService.getTrainingById(id));
    }

    @Test
    public void testGetAllTrainings() {
        Training training1 = new Training("T0", "TR0", "Test1",
                TrainingType.BALANCE, null, null);
        Training training2 = new Training("T1", "TR1", "Test2",
                TrainingType.STRENGTH, null, null);
        Training training3 = new Training("T2", "TR2", "Test3",
                TrainingType.STRENGTH, null, null);

        List<Training> expectedTrainings = List.of(training1, training2, training3);

        when(trainingDao.findAll()).thenReturn(expectedTrainings);

        List<Training> result = trainingService.getAllTrainings();

        verify(trainingDao).findAll();
        assertEquals(expectedTrainings.size(), result.size());
        assertEquals(expectedTrainings.get(0).getTrainingName(), result.get(0).getTrainingName());
        assertEquals(expectedTrainings.get(1).getTrainingName(), result.get(1).getTrainingName());
        assertEquals(expectedTrainings.get(2).getTrainingName(), result.get(2).getTrainingName());
    }
}

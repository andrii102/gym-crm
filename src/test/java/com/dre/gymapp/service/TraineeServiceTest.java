package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.util.CredentialsGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @InjectMocks
    private TraineeService traineeService;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private CredentialsGenerator credentialsGenerator;

    @Test
    public void testCreateTrainee() {
        String username = "john.doe";
        String password = "1234567890";
        Trainee inputTrainee = new Trainee("T0", "John", "Doe");
        Trainee expectedTrainee = new Trainee("T0", "John", "Doe");
        expectedTrainee.setUsername(username);
        expectedTrainee.setPassword(password);

        when(credentialsGenerator.generateUsername(eq("John"), eq("Doe"),any())).thenReturn(username);
        when(credentialsGenerator.generatePassword()).thenReturn(password);
        when(traineeDao.save(any(Trainee.class))).thenReturn(expectedTrainee);

        Trainee result = traineeService.createTrainee(inputTrainee);

        assertNotNull(result);
        assertEquals(expectedTrainee.getUserId(), result.getUserId());
        assertEquals(expectedTrainee.getUsername(), result.getUsername());
        assertEquals(expectedTrainee.getPassword(), result.getPassword());

        verify(credentialsGenerator).generateUsername(eq("John"), eq("Doe"), any());
        verify(credentialsGenerator).generatePassword();
        verify(traineeDao).save(any(Trainee.class));
    }

    @Test
    public void  updatesAnExistingTraineeRecord() {
        Trainee inputTrainee = new Trainee("T0", "John", "Doe");
        Trainee expectedTrainee = new Trainee("T0", "John", "Updated");

        when(traineeDao.update(inputTrainee)).thenReturn(expectedTrainee);

        Trainee result = traineeService.updateTrainee(inputTrainee);

        verify(traineeDao).update(inputTrainee);
        assertEquals(expectedTrainee.getUserId(), result.getUserId());
        assertEquals(expectedTrainee.getFirstName(), result.getFirstName());
        assertEquals(expectedTrainee.getLastName(), result.getLastName());
    }

    @Test
    public void testUpdateTraineeNonExistingTrainee() {
        Trainee inputTrainee = new Trainee("T0", "John", "Doe");

        when(traineeDao.update(inputTrainee)).thenReturn(null);

        Trainee trainee = traineeService.updateTrainee(inputTrainee);

        verify(traineeDao).update(inputTrainee);
        assertNull(trainee);
    }

    @Test
    public void testGetTraineeByIdExistingTrainee() {
        String id = "T0";
        Trainee expectedTrainee = new Trainee(id, "John", "Doe");

        when(traineeDao.findById(id)).thenReturn(Optional.of(expectedTrainee));

        Trainee result = traineeService.getTraineeById(id);

        verify(traineeDao).findById(id);
        assertEquals(expectedTrainee.getUserId(), result.getUserId());
        assertEquals(expectedTrainee.getFirstName(), result.getFirstName());
        assertEquals(expectedTrainee.getLastName(), result.getLastName());
    }

    @Test
    public void testGetTraineeByIdNonExistingTrainee() {
        String id = "T0";

        when(traineeDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> traineeService.getTraineeById(id) );
    }

    @Test
    public void testGetAllTrainees() {
        Trainee trainee1 = new Trainee("T0", "John", "Doe");
        Trainee trainee2 = new Trainee("T1", "Jane", "Doe");
        Trainee trainee3 = new Trainee("T2", "John", "Smith");

        when(traineeDao.findAll()).thenReturn(List.of(trainee1, trainee2, trainee3));

        List<Trainee> result = traineeService.getAllTrainees();

        verify(traineeDao).findAll();
        assertEquals(3, result.size());
        assertEquals(trainee1.getUserId(), result.get(0).getUserId());
        assertEquals(trainee1.getFirstName(), result.get(0).getFirstName());
    }

    @Test
    public void testDeleteTraineeByIdExistingTrainee() {
        String id = "T0";

        traineeService.deleteTraineeById(id);

        verify(traineeDao).deleteById(id);
    }

}

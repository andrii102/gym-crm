package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainer;
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
public class TrainerServiceTest {
    @InjectMocks
    private  TrainerService trainerService;
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private CredentialsGenerator credentialsGenerator;

    @Test
    public void testCreateTrainer() {
        String username = "john.doe";
        String password = "1234567890";
        Trainer inputTrainer = new Trainer("T0", "John", "Doe");
        Trainer expectedTrainer = new Trainer("T0", "John", "Doe");
        expectedTrainer.setUsername(username);
        expectedTrainer.setPassword(password);

        when(credentialsGenerator.generateUsername(eq("John"), eq("Doe"),any())).thenReturn(username);
        when(credentialsGenerator.generatePassword()).thenReturn(password);
        when(trainerDao.save(any(Trainer.class))).thenReturn(expectedTrainer);

        Trainer result = trainerService.createTrainer(inputTrainer);

        assertNotNull(result);
        assertEquals(expectedTrainer.getUserId(), result.getUserId());
        assertEquals(expectedTrainer.getUsername(), result.getUsername());
        assertEquals(expectedTrainer.getPassword(), result.getPassword());

        verify(credentialsGenerator).generateUsername(eq("John"), eq("Doe"), any());
        verify(credentialsGenerator).generatePassword();
        verify(trainerDao).save(any(Trainer.class));
    }

    @Test
    public void testGetTrainerByIdExistingTrainer() {
        String id = "T0";
        Trainer expectedTrainer = new Trainer(id, "John", "Doe");

        when(trainerDao.findById(id)).thenReturn(Optional.of(expectedTrainer));

        Trainer result = trainerService.getTrainerById(id);

        verify(trainerDao).findById(id);
        assertEquals(expectedTrainer.getUserId(), result.getUserId());
        assertEquals(expectedTrainer.getFirstName(), result.getFirstName());
        assertEquals(expectedTrainer.getLastName(), result.getLastName());
    }

    @Test
    public void testGetTrainerByIdNonExistingTrainer() {
        String id = "T0";

        when(trainerDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> trainerService.getTrainerById(id));
    }

    @Test
    public void testGetAllTrainers() {
        Trainer trainer1 = new Trainer("T0", "John", "Doe");
        Trainer trainer2 = new Trainer("T1", "Jane", "Doe");
        Trainer trainer3 = new Trainer("T2", "John", "Smith");

        when(trainerDao.findAll()).thenReturn(List.of(trainer1, trainer2, trainer3));

        List<Trainer> result = trainerService.getAllTrainers();

        verify(trainerDao).findAll();
        assertEquals(3, result.size());
        assertEquals(trainer1.getUserId(), result.get(0).getUserId());
        assertEquals(trainer1.getFirstName(), result.get(0).getFirstName());
    }

    @Test
    public void testUpdateExistingTrainer() {
        Trainer inputTrainer = new Trainer("T0", "John", "Doe");
        Trainer expectedTrainer = new Trainer("T0", "John", "Updated");

        when(trainerDao.update(inputTrainer)).thenReturn(expectedTrainer);

        Trainer result = trainerService.updateTrainer(inputTrainer);

        verify(trainerDao).update(inputTrainer);
        assertEquals(expectedTrainer.getUserId(), result.getUserId());
        assertEquals(expectedTrainer.getLastName(), result.getLastName());
    }

    @Test
    public void testUpdateNonExistingTrainer() {
        Trainer inputTrainer = new Trainer("T0", "John", "Doe");

        when(trainerDao.update(inputTrainer)).thenReturn(null);

        Trainer trainer = trainerService.updateTrainer(inputTrainer);

        verify(trainerDao).update(inputTrainer);
        assertNull(trainer);
    }
}

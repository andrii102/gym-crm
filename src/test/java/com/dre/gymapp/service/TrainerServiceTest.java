package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.dto.TrainerProfileUpdateRequest;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.User;
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
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("John", "Doe");
        testUser.setUsername("john.doe");
        testUser.setPassword("<PASSWORD>");
    }

    @Test
    public void createTrainer_ShouldCreateNewTrainerRecord(){
        when(userService.createUser("John", "Doe")).thenReturn(testUser);
        Trainer result = trainerService.createTrainer("John", "Doe");

        assertNotNull(result);
        assertEquals(testUser, result.getUser());

        verify(trainerDao).save(any(Trainer.class));
    }

    @Test
    public void changePassword_ShouldChangePassword(){
        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        trainerService.changePassword(testUser.getFirstName(), testUser.getPassword(), "newPassword");

        verify(userService).changePassword(any(), any());
    }

    @Test
    public void getTrainerById_ShouldReturnTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUser(testUser);

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        when(trainerDao.findById(any())).thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getTrainerById(testUser.getFirstName(), testUser.getPassword(), trainer.getId());

        assertNotNull(result);
        assertEquals(trainer.getId(), result.getId());
        assertEquals(trainer.getUser().getUsername(), result.getUser().getUsername());

        verify(trainerDao).findById(eq(trainer.getId()));
    }

    @Test
    public void getTrainerById_ShouldThrowException_TrainerNotFound() {
        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        when(trainerDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> trainerService.getTrainerById(testUser.getFirstName(), testUser.getPassword(), 1L));

        verify(trainerDao).findById(any());
    }

    @Test
    public void getAllTrainers_ShouldReturnAllTrainers() {
        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        when(trainerDao.findAll()).thenReturn(List.of(new Trainer(), new Trainer()));
        List<Trainer> trainers = trainerService.getAllTrainers(testUser.getFirstName(), testUser.getPassword());

        assertNotNull(trainers);
        assertEquals(2, trainers.size());

        verify(trainerDao).findAll();
    }

    @Test
    public void updateTrainer_NullFieldsInRequest_ShouldUpdateTrainerRecord() {
        Trainer trainer = new Trainer();
        TrainerProfileUpdateRequest trainerProfileUpdateRequest = new TrainerProfileUpdateRequest();

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        when(trainerDao.findById(any())).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(trainer);

        Trainer result = trainerService.updateTrainer(testUser.getFirstName(), testUser.getPassword(), trainerProfileUpdateRequest);

        assertNotNull(result);
        assertEquals(trainer, result);

        verify(userService).updateUser(any());
        verify(trainerDao).update(any());
    }

    @Test
    public void updateTrainer_NonNullFieldsInRequest_ShouldUpdateTraineeRecord(){
        Trainer trainer = new Trainer();
        User user = new User("John", "Doe");
        user.setUsername("john.doe");
        user.setPassword("12345");
        trainer.setUser(user);

        TrainerProfileUpdateRequest request = new TrainerProfileUpdateRequest();
        request.setFirstName("NEW_FN");
        request.setLastName("NEW_LN");
        request.setUsername("newUsername");

        User updatedUser = new User("NEW_FN", "NEW_LN");
        updatedUser.setUsername("newUsername");
        Trainer expectedTrainer = new Trainer(null, updatedUser);

        when(userService.authenticateUser(any(), any())).thenReturn(user);
        when(trainerDao.findById(any())).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(trainer);

        Trainer result = trainerService.updateTrainer(user.getFirstName(), user.getPassword(), request);

        assertNotNull(result);
        assertEquals(expectedTrainer.getSpecialization(), result.getSpecialization());
        assertEquals(expectedTrainer.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(expectedTrainer.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(expectedTrainer.getUser().getUsername(), result.getUser().getUsername());

        verify(userService).updateUser(any());
        verify(trainerDao).update(any());
    }

    @Test
    public void findUnassignedTrainers(){
        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        when(trainerDao.findUnassignedTrainers()).thenReturn(List.of(new Trainer(), new Trainer()));

        List<Trainer> trainers = trainerService.findUnassignedTrainers(testUser.getFirstName(), testUser.getPassword());

        assertNotNull(trainers);
        assertEquals(2, trainers.size());

        verify(trainerDao).findUnassignedTrainers();
    }

    @Test
    public void activateTrainer_ShouldActivateTrainer(){
        testUser.setActive(false);

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        trainerService.activateTrainer(testUser.getUsername(), testUser.getPassword());

        verify(userService).setActive(any(), eq(true));
    }

    @Test
    public void deactivateTrainer_ShouldDeactivateTrainer(){
        testUser.setActive(true);

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        trainerService.deactivateTrainer(testUser.getUsername(), testUser.getPassword());

        verify(userService).setActive(any(), eq(false));
    }
}

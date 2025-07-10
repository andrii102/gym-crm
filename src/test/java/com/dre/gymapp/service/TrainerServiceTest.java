package com.dre.gymapp.service;

import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.dao.TrainingTypeDao;
import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TrainerRegistrationRequest;
import com.dre.gymapp.dto.trainer.TrainerProfileResponse;
import com.dre.gymapp.dto.trainer.TrainerProfileUpdateRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.exception.BadRequestException;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.TrainingType;
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
    @Mock
    private TrainingTypeDao trainingTypeDao;
    @Mock
    private TraineeService traineeService;

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
        when(trainingTypeDao.findById(any())).thenReturn(new TrainingType());

        RegistrationResponse result = trainerService.createTrainer(new TrainerRegistrationRequest("John", "Doe", null));

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getPassword(), result.getPassword());

        verify(trainerDao).save(any(Trainer.class));
    }

    @Test
    public void changePassword_ShouldChangePassword(){
        trainerService.changePassword(testUser.getFirstName(), "newPassword");

        verify(userService).changePassword(any(), any());
    }

    @Test
    public void getTrainerById_ShouldReturnTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUser(testUser);

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        when(trainerDao.findById(any())).thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getTrainerById(trainer.getId());

        assertNotNull(result);
        assertEquals(trainer.getId(), result.getId());
        assertEquals(trainer.getUser().getUsername(), result.getUser().getUsername());

        verify(trainerDao).findById(eq(trainer.getId()));
    }

    @Test
    public void getTrainerById_ShouldThrowException_TrainerNotFound() {
        when(trainerDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> trainerService.getTrainerById( 1L));

        verify(trainerDao).findById(any());
    }

    @Test
    public void getAllTrainers_ShouldReturnAllTrainers() {
        when(trainerDao.findAll()).thenReturn(List.of(new Trainer(), new Trainer()));
        List<Trainer> trainers = trainerService.getAllTrainers();

        assertNotNull(trainers);
        assertEquals(2, trainers.size());

        verify(trainerDao).findAll();
    }

    @Test
    public void updateTrainer_NullTrainingTypeInRequest_ShouldThrowException() {
        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType("RUNNING");
        TrainerProfileUpdateRequest trainerProfileUpdateRequest = new TrainerProfileUpdateRequest();
        trainer.setSpecialization(trainingType);
        trainerProfileUpdateRequest.setTrainingType("CARDIO");

        when(userService.getUserByUsername(any())).thenReturn(testUser);
        when(trainerDao.findById(any())).thenReturn(Optional.of(trainer));

        assertThrows(BadRequestException.class, () ->
                trainerService.updateTrainer(testUser.getFirstName(), trainerProfileUpdateRequest));
    }

    @Test
    public void updateTrainer_NonNullFieldsInRequest_ShouldUpdateTraineeRecord(){
        Trainer trainer = new Trainer();
        User user = new User("John", "Doe");
        user.setUsername("john.doe");
        user.setPassword("12345");
        trainer.setUser(user);
        TrainingType trainingType = new TrainingType("RUNNING");
        trainer.setSpecialization(trainingType);

        TrainerProfileUpdateRequest request = new TrainerProfileUpdateRequest();
        request.setFirstName("NEW_FN");
        request.setLastName("NEW_LN");
        request.setUsername("newUsername");
        request.setTrainingType(trainingType.getTrainingTypeName());

        User updatedUser = new User("NEW_FN", "NEW_LN");
        updatedUser.setUsername("newUsername");
        Trainer expectedTrainer = new Trainer(trainingType, updatedUser);

        when(userService.getUserByUsername(any())).thenReturn(user);
        when(trainerDao.findById(any())).thenReturn(Optional.of(trainer));
        when(userService.updateUser(any())).thenReturn(updatedUser);
        when(trainerDao.update(any())).thenReturn(expectedTrainer);

        TrainerProfileResponse result = trainerService.updateTrainer(user.getFirstName(), request);

        assertNotNull(result);
        assertEquals(expectedTrainer.getSpecialization().getTrainingTypeName(), result.getTrainingType());
        assertEquals(expectedTrainer.getUser().getFirstName(), result.getFirstName());
        assertEquals(expectedTrainer.getUser().getLastName(), result.getLastName());

        verify(userService).updateUser(any());
        verify(trainerDao).update(any());
    }

    @Test
    public void getUnassignedTrainers(){
        Trainee trainee = new Trainee();
        trainee.setUser(testUser);

        Trainer trainer1 = new Trainer(new TrainingType("RUNNING"), new User("user", "1"));
        Trainer trainer2 = new Trainer(new TrainingType("CARDIO"), new User("user2", "2"));

        when(traineeService.getTraineeByUsername(any())).thenReturn(trainee);
        when(trainerDao.findUnassignedTrainersOnTrainee(any())).thenReturn(List.of(trainer1, trainer2));

        List<TrainerShortProfile> trainers = trainerService.getUnassignedTrainers(testUser.getUsername());

        assertNotNull(trainers);
        assertEquals(2, trainers.size());

        verify(trainerDao).findUnassignedTrainersOnTrainee(any());
    }

    @Test
    public void activateTrainer_ShouldActivateTrainer(){
        testUser.setActive(false);

        when(userService.getUserByUsername(any())).thenReturn(testUser);

        trainerService.activateTrainer(testUser.getUsername());

        verify(userService).setActive(any(), eq(true));
    }

    @Test
    public void deactivateTrainer_ShouldDeactivateTrainer(){
        testUser.setActive(true);

        when(userService.getUserByUsername(any())).thenReturn(testUser);

        trainerService.deactivateTrainer(testUser.getUsername());

        verify(userService).setActive(any(), eq(false));
    }
}

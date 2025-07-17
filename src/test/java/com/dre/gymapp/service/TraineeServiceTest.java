package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.dao.TrainerDao;
import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TraineeRegistrationRequest;
import com.dre.gymapp.dto.trainee.TraineeProfileResponse;
import com.dre.gymapp.dto.trainee.TraineeProfileUpdateRequest;
import com.dre.gymapp.dto.trainee.UpdateTrainersListRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.dto.trainings.TraineeTrainingsRequest;
import com.dre.gymapp.dto.trainings.TraineeTrainingsResponse;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @InjectMocks
    private TraineeService traineeService;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private UserService userService;
    @Mock
    private TrainingService trainingService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("John", "Doe");
        testUser.setUsername("john.doe");
        testUser.setPassword("<PASSWORD>");
    }

    @Test
    public void createTrainee_ShouldCreateNewTraineeRecord() {
        when(userService.createUser(any(), any())).thenReturn(testUser);

        RegistrationResponse result = traineeService.createTrainee(new TraineeRegistrationRequest("John", "Doe"));

        assertNotNull(result);
        verify(traineeDao).save(any(Trainee.class));
    }

    @Test
    public void updateTrainee_NullFieldsInRequest_ShouldUpdateTraineeRecord() {
        Trainee trainee = new Trainee();
        TraineeProfileUpdateRequest traineeProfileUpdateRequest = new TraineeProfileUpdateRequest(testUser.getUsername(), null,
                null, true);

        when(userService.getUserByUsername(any())).thenReturn(testUser);
        when(traineeDao.findByUserId(any())).thenReturn(Optional.of(trainee));
        when(userService.updateUser(any())).thenReturn(testUser);
        when(traineeDao.update(any())).thenReturn(trainee);

        TraineeProfileResponse result = traineeService.updateTrainee(testUser.getFirstName(), traineeProfileUpdateRequest);

        assertNotNull(result);
        assertEquals(testUser.getFirstName(), result.getFirstName());
        assertEquals(testUser.getLastName(), result.getLastName());

        verify(userService).updateUser(any());
        verify(traineeDao).update(any());
    }

    @Test
    public void updateTrainee_NonNullFieldsInRequest_ShouldUpdateTraineeRecord() {
        Trainee trainee = new Trainee();
        trainee.setUser(testUser);
        Trainer trainer = new Trainer(new TrainingType("RUNNING"));
        User trainerUser = new User("Trainer", "User");
        trainerUser.setUsername("trainer.user");
        trainer.setUser(trainerUser);
        trainee.setTrainers(List.of(trainer));

        TraineeProfileUpdateRequest request = new TraineeProfileUpdateRequest();
        request.setFirstName("NEW_FN");
        request.setLastName("NEW_LN");
        request.setUsername(testUser.getUsername());
        request.setAddress("123 Main St");

        User updatedUser = new User("NEW_FN", "NEW_LN");
        updatedUser.setUsername(testUser.getUsername());
        Trainee expectedTrainee = new Trainee(null, "123 Main St");
        expectedTrainee.setUser(updatedUser);

        when(userService.getUserByUsername(any())).thenReturn(testUser);
        when(traineeDao.findByUserId(any())).thenReturn(Optional.of(trainee));
        when(userService.updateUser(any())).thenReturn(updatedUser);
        when(traineeDao.update(any())).thenReturn(trainee);

        TraineeProfileResponse result = traineeService.updateTrainee(testUser.getFirstName(), request);

        assertNotNull(result);
        assertEquals(expectedTrainee.getUser().getFirstName(), result.getFirstName());
        assertEquals(expectedTrainee.getUser().getLastName(), result.getLastName());
        assertEquals(expectedTrainee.getAddress(), result.getAddress());

        verify(userService).updateUser(any());
        verify(traineeDao).update(any());
    }

    @Test
    public void changePassword_ShouldChangePassword(){
        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        traineeService.changePassword(testUser.getFirstName(), testUser.getPassword(), "newPassword");

        verify(userService).changePassword(any(), any());
    }

    @Test
    public void getTraineeById_ShouldReturnTrainee() {
        Trainee trainee = new Trainee();

        when(traineeDao.findById(any())).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getTraineeById(trainee.getId());

        assertNotNull(result);
        assertEquals(trainee.getId(), result.getId());

        verify(traineeDao).findById(eq(trainee.getId()));
    }

    @Test
    public void getTraineeById_ShouldThrowException_TraineeNotFound() {
        when(traineeDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> traineeService.getTraineeById(1L));

        verify(traineeDao).findById(any());
    }

    @Test
    public void getTraineeByProfileByUsername_ShouldReturnTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUser(testUser);
        User trainerUser = new User("Trainer", "User");
        trainerUser.setUsername("trainer.user");
        Trainer trainer = new Trainer(new TrainingType("RUNNING"), trainerUser);
        trainee.setTrainers(List.of(trainer));

        when(traineeDao.findByUsername(any())).thenReturn(Optional.of(trainee));

        TraineeProfileResponse result = traineeService.getTraineeProfileByUsername(testUser.getUsername());

        assertNotNull(result);
        assertEquals(trainee.getUser().getFirstName(), result.getFirstName());
        assertEquals(trainee.getUser().getLastName(), result.getLastName());
        assertEquals(trainee.getTrainers().size(), result.getTrainers().size());

        verify(traineeDao).findByUsername(eq(testUser.getUsername()));
    }

    @Test
    public void getAllTrainees_ShouldReturnAllTrainees() {
        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        when(traineeDao.findAll()).thenReturn(List.of(new Trainee(), new Trainee()));
        List<Trainee> trainees = traineeService.getAllTrainees(testUser.getFirstName(), testUser.getPassword());

        assertNotNull(trainees);
        assertEquals(2, trainees.size());

        verify(traineeDao).findAll();
    }

    @Test
    public void deleteTraineeById_ShouldDeleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);

        traineeService.deleteTraineeById( trainee.getId());

        verify(traineeDao).deleteById(eq(trainee.getId()));
    }

    @Test
    public void deleteTraineeById_ShouldThrowException_TraineeNotFound() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);

        doThrow(NotFoundException.class).when(traineeDao).deleteById(any());

        assertThrows(NotFoundException.class,
                () -> traineeService.deleteTraineeById(trainee.getId()));

        verify(traineeDao).deleteById(eq(trainee.getId()));
    }

    @Test
    public void deleteTraineeByUsername_ShouldDeleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUser(testUser);

        when(traineeDao.findByUsername(any())).thenReturn(Optional.of(trainee));

        traineeService.deleteTraineeByUsername(trainee.getUser().getUsername());

        verify(traineeDao).delete(eq(trainee));
    }

    @Test
    public void updateTraineeTrainerList_ShouldUpdateTrainerList() {
        Trainee trainee = new Trainee();
        trainee.setUser(testUser);

        User trainerUser = new User("Trainer", "User");
        trainerUser.setUsername("trainer.user");
        Trainer trainer = new Trainer(new TrainingType("RUNNING"), trainerUser);

        UpdateTrainersListRequest request = new UpdateTrainersListRequest();
        request.setTrainers(List.of(trainerUser.getUsername()));

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setTrainers(List.of(trainer));

        when(traineeDao.findByUsername(any())).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsernames(any())).thenReturn(List.of(trainer));
        when(traineeDao.update(any())).thenReturn(updatedTrainee);

        List<TrainerShortProfile> result = traineeService.updateTraineeTrainerList(testUser.getUsername(), request);

        assertEquals(updatedTrainee.getTrainers().size(), result.size());

        verify(traineeDao).update(any());
    }

    @Test
    public void getTraineeTrainings_ShouldReturnTraineeTrainings() {
        Trainee trainee = new Trainee();
        trainee.setUser(testUser);
        User trainerUser = new User("Trainer", "User");
        trainerUser.setUsername("trainer.user");
        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        Training training = new Training(trainee, trainer, "Training Name",
                new TrainingType("RUNNING"), LocalDate.of(2025, 1, 1), 60);
        trainee.setTrainings(List.of(training));

        TraineeTrainingsRequest request = new TraineeTrainingsRequest();


        when(trainingService.getTrainingsByParams(any(), any(), any(), any(), any())).thenReturn(List.of(training));

        List<TraineeTrainingsResponse> result = traineeService.getTraineeTrainings(trainee.getUser().getUsername(), request);

        assertNotNull(result);
        assertEquals(trainee.getTrainings().size(), result.size());

        verify(trainingService).getTrainingsByParams(any(), any(), any(), any(), any());
    }

    @Test
    public void setTraineeActiveStatus_ShouldActivateTrainee(){
        testUser.setActive(false);

        when(userService.getUserByUsername(any())).thenReturn(testUser);

        traineeService.setTraineeActiveStatus(testUser.getUsername(), true);

        verify(userService).setActive(any(), eq(true));
    }

    @Test
    public void setTraineeActiveStatus_ShouldDeactivateTrainee(){
        testUser.setActive(true);

        when(userService.getUserByUsername(any())).thenReturn(testUser);

        traineeService.setTraineeActiveStatus(testUser.getUsername(), false);

        verify(userService).setActive(any(), eq(false));
    }

}

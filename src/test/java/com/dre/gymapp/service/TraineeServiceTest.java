package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TraineeRegistrationRequest;
import com.dre.gymapp.dto.trainee.TraineeProfileResponse;
import com.dre.gymapp.dto.trainee.TraineeProfileUpdateRequest;
import com.dre.gymapp.dto.trainee.UpdateTrainersListRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @InjectMocks
    private TraineeService traineeService;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private UserService userService;
    @Mock
    private TrainerService trainerService;

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
        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        when(traineeDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> traineeService.getTraineeById(1L));

        verify(traineeDao).findById(any());
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

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
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
        when(trainerService.getTrainersByUsernames(any())).thenReturn(List.of(trainer));
        when(traineeDao.update(any())).thenReturn(updatedTrainee);

        List<TrainerShortProfile> result = traineeService.updateTraineeTrainerList(testUser.getUsername(), request);

        assertEquals(updatedTrainee.getTrainers().size(), result.size());

        verify(traineeDao).update(any());
    }

    @Test
    public void activateTrainee_ShouldActivateTrainee(){
        testUser.setActive(false);

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        traineeService.activateTrainee(testUser.getUsername(), testUser.getPassword());

        verify(userService).setActive(any(), eq(true));
    }

    @Test
    public void deactivateTrainee_ShouldDeactivateTrainee(){
        testUser.setActive(true);

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        traineeService.deactivateTrainee(testUser.getUsername(), testUser.getPassword());

        verify(userService).setActive(any(), eq(false));
    }


}

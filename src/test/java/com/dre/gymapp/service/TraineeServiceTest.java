package com.dre.gymapp.service;

import com.dre.gymapp.dao.TraineeDao;
import com.dre.gymapp.dto.TraineeProfileUpdateRequest;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.model.Trainee;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @InjectMocks
    private TraineeService traineeService;
    @Mock
    private TraineeDao traineeDao;
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
    public void createTrainee_ShouldCreateNewTraineeRecord() {
        when(userService.createUser("John", "Doe")).thenReturn(testUser);

        Trainee result = traineeService.createTrainee("John", "Doe");

        assertNotNull(result);
        assertEquals(testUser, result.getUser());

        verify(traineeDao).save(any(Trainee.class));
    }

    @Test
    public void updateTrainee_NullFieldsInRequest_ShouldUpdateTraineeRecord() {
        Trainee trainee = new Trainee();
        TraineeProfileUpdateRequest traineeProfileUpdateRequest = new TraineeProfileUpdateRequest();

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        when(traineeDao.findById(any())).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any())).thenReturn(trainee);

        Trainee result = traineeService.updateTrainee(testUser.getFirstName(), testUser.getPassword(), traineeProfileUpdateRequest);

        assertNotNull(result);
        assertEquals(trainee, result);

        verify(userService).updateUser(any());
        verify(traineeDao).update(any());
    }

    @Test
    public void updateTrainee_NonNullFieldsInRequest_ShouldUpdateTraineeRecord() {
        Trainee trainee = new Trainee();
        User user = new User("John", "Doe");
        user.setUsername("john.doe");
        trainee.setUser(user);

        TraineeProfileUpdateRequest request = new TraineeProfileUpdateRequest();
        request.setFirstName("NEW_FN");
        request.setLastName("NEW_LN");
        request.setUsername("newUsername");
        request.setAddress("123 Main St");

        User updatedUser = new User("NEW_FN", "NEW_LN");
        updatedUser.setUsername("newUsername");
        Trainee expectedTrainee = new Trainee(null, "123 Main St");
        expectedTrainee.setUser(updatedUser);

        when(userService.authenticateUser(any(), any())).thenReturn(user);
        when(traineeDao.findById(any())).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any())).thenReturn(trainee);

        Trainee result = traineeService.updateTrainee(user.getFirstName(), user.getPassword(), request);

        assertNotNull(result);
        assertEquals(expectedTrainee.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(expectedTrainee.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(expectedTrainee.getUser().getUsername(), result.getUser().getUsername());
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

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        when(traineeDao.findById(any())).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getTraineeById(testUser.getFirstName(), testUser.getPassword(), trainee.getId());

        assertNotNull(result);
        assertEquals(trainee.getId(), result.getId());

        verify(traineeDao).findById(eq(trainee.getId()));
    }

    @Test
    public void getTraineeById_ShouldThrowException_TraineeNotFound() {
        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        when(traineeDao.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> traineeService.getTraineeById(testUser.getFirstName(), testUser.getPassword(), 1L));

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

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        traineeService.deleteTraineeById(testUser.getFirstName(), testUser.getPassword(), trainee.getId());

        verify(traineeDao).deleteById(eq(trainee.getId()));
    }

    @Test
    public void deleteTraineeById_ShouldThrowException_TraineeNotFound() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);
        doThrow(NotFoundException.class).when(traineeDao).deleteById(any());

        assertThrows(NotFoundException.class,
                () -> traineeService.deleteTraineeById(testUser.getFirstName(), testUser.getPassword(), trainee.getId()));

        verify(traineeDao).deleteById(eq(trainee.getId()));
    }

    @Test
    public void deleteTraineeByUsername_ShouldDeleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUser(testUser);

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        traineeService.deleteTraineeByUsername(testUser.getFirstName(), testUser.getPassword(), trainee.getUser().getUsername());

        verify(userService).deleteUserById(eq(trainee.getUser().getId()));
        verify(traineeDao).deleteById(eq(trainee.getId()));
    }

    @Test
    public void addTrainer_ShouldAddTrainerToTrainee() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        when(userService.authenticateUser(any(), any())).thenReturn(testUser);

        traineeService.addTrainer(testUser.getUsername(), testUser.getPassword(), trainee, trainer);

        assertTrue(trainee.getTrainers().contains(trainer));
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

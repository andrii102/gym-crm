package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainee.TraineeProfileResponse;
import com.dre.gymapp.dto.trainee.TraineeProfileUpdateRequest;
import com.dre.gymapp.dto.trainee.UpdateTrainersListRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.dto.trainings.TraineeTrainingsRequest;
import com.dre.gymapp.dto.trainings.TraineeTrainingsResponse;
import com.dre.gymapp.dto.user.ActivationRequest;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {
    @InjectMocks
    private TraineeController traineeController;
    @Mock
    private TraineeService traineeService;

    @Test
    public void getTraineeProfile_ShouldReturnProfile() {
        String username = "john.doe";
        TraineeProfileResponse expectedResponse = new TraineeProfileResponse();
        expectedResponse.setFirstName("John");
        expectedResponse.setLastName("Doe");
        expectedResponse.setActive(true);

        when(traineeService.getTraineeProfileByUsername(username)).thenReturn(expectedResponse);

        ResponseEntity<TraineeProfileResponse> response = traineeController.getTraineeProfile(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse.getFirstName(), response.getBody().getFirstName());
        assertEquals(expectedResponse.getLastName(), response.getBody().getLastName());

        verify(traineeService).getTraineeProfileByUsername(username);
    }

    @Test
    public void getTraineeProfile_ShouldReturnNotFound() {
        String username = "john.doe";

        when(traineeService.getTraineeProfileByUsername(username)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> traineeController.getTraineeProfile(username));
    }

    @Test
    public void updateTraineeProfile_ShouldReturnProfile() {
        String username = "john.doe";
        TraineeProfileUpdateRequest request = new TraineeProfileUpdateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setUsername(username);
        request.setActive(true);
        TraineeProfileResponse expectedResponse = new TraineeProfileResponse();
        expectedResponse.setFirstName("John");
        expectedResponse.setLastName("Doe");
        expectedResponse.setActive(true);

        when(traineeService.updateTrainee(username, request)).thenReturn(expectedResponse);

        ResponseEntity<TraineeProfileResponse> response = traineeController.updateTraineeProfile(username, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse.getFirstName(), response.getBody().getFirstName());
        assertEquals(expectedResponse.getLastName(), response.getBody().getLastName());

        verify(traineeService).updateTrainee(username, request);
    }

    @Test
    public void updateTraineeProfile_ShouldThrowNotFound() {
        String username = "john.doe";
        TraineeProfileUpdateRequest request = new TraineeProfileUpdateRequest();

        when(traineeService.updateTrainee(username, request)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> traineeController.updateTraineeProfile(username, request));
    }

    @Test
    public void deleteTraineeProfile_ShouldDeleteProfile() {
        String username = "john.doe";

        ResponseEntity<String> response =  traineeController.deleteTrainee(username);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(traineeService).deleteTraineeByUsername(username);
    }

    @Test
    public void deleteTraineeProfile_ShouldThrowNotFound() {
        String username = "john.doe";

        doThrow(NotFoundException.class).when(traineeService).deleteTraineeByUsername(username);

        assertThrows(NotFoundException.class, () -> traineeController.deleteTrainee(username));
    }

    @Test
    public void updateTraineeTrainerList_ShouldUpdate() {
        String username = "trainee";
        List<String> trainers = List.of("john.doe", "john.doe1");
        TrainerShortProfile trainer1 = new TrainerShortProfile("John", "Doe", "john.doe", null);
        TrainerShortProfile trainer2 = new TrainerShortProfile("John", "Doe", "john.doe1", null);
        UpdateTrainersListRequest request = new UpdateTrainersListRequest();
        request.setTrainers(trainers);

        List<TrainerShortProfile> expectedResponse = List.of(trainer1, trainer2);

        when(traineeService.updateTraineeTrainerList(username,  request)).thenReturn(expectedResponse);

        ResponseEntity<List<TrainerShortProfile>> response = traineeController.updateTraineeTrainersList(username, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), expectedResponse);
    }

    @Test
    public void getTraineeTrainings_ShouldReturnTraineeTrainings() {
        String username = "john.doe";
        List<TraineeTrainingsResponse> expectedResponse = List.of(new TraineeTrainingsResponse());
        TraineeTrainingsRequest request = new TraineeTrainingsRequest();

        when(traineeService.getTraineeTrainings(username, request)).thenReturn(expectedResponse);

        ResponseEntity<List<TraineeTrainingsResponse>> response = traineeController.getTraineeTrainings(username, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), expectedResponse);

        verify(traineeService).getTraineeTrainings(username, request);
    }

    @Test
    public void getTraineeTrainings_ShouldThrowNotFound() {
        String username = "john.doe";
        TraineeTrainingsRequest request = new TraineeTrainingsRequest();

        when(traineeService.getTraineeTrainings(username, request)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> traineeController.getTraineeTrainings(username, request));
    }

    @Test
    public void updateActiveStatus_ShouldUpdate() {
        String username = "john.doe";

        ResponseEntity<String> response = traineeController.updateActiveStatus(username, new ActivationRequest(true));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(traineeService).setTraineeActiveStatus(username, true);
    }

    @Test
    public void updateActiveStatus_ShouldThrowNotFound() {
        String username = "john.doe";

        doThrow(NotFoundException.class).when(traineeService).setTraineeActiveStatus(username, true);

       assertThrows(NotFoundException.class, () -> traineeController.updateActiveStatus(username, new ActivationRequest(true)));
    }

}

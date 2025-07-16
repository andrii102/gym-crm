package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainer.TrainerProfileResponse;
import com.dre.gymapp.dto.trainer.TrainerProfileUpdateRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.dto.trainings.TrainerTrainingsRequest;
import com.dre.gymapp.dto.trainings.TrainerTrainingsResponse;
import com.dre.gymapp.dto.user.ActivationRequest;
import com.dre.gymapp.exception.BadRequestException;
import com.dre.gymapp.exception.NotFoundException;
import com.dre.gymapp.service.TrainerService;
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
public class TrainerControllerTest {
    @InjectMocks
    private TrainerController trainerController;
    @Mock
    private TrainerService trainerService;

    @Test
    public void getTrainerProfile_ShouldReturnProfile() {
        String username = "john.doe";
        TrainerProfileResponse expectedResponse = new TrainerProfileResponse();
        expectedResponse.setFirstName("John");
        expectedResponse.setLastName("Doe");
        expectedResponse.setActive(true);

        when(trainerService.getTrainerProfileByUsername(username)).thenReturn(expectedResponse);

        ResponseEntity<TrainerProfileResponse> response = trainerController.getTrainerProfile(username);

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getFirstName(), expectedResponse.getFirstName());
        assertEquals(response.getBody().getLastName(), expectedResponse.getLastName());
    }

    @Test
    public void getTrainerProfile_ShouldReturnNotFound() {
        String username = "john.doe";

        when(trainerService.getTrainerProfileByUsername(username)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> trainerController.getTrainerProfile(username));
    }

    @Test
    public void updateTrainerProfile_ShouldReturnProfile() {
        String username = "john.doe";
        TrainerProfileUpdateRequest request = new TrainerProfileUpdateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setUsername(username);
        request.setActive(true);
        TrainerProfileResponse expectedResponse = new TrainerProfileResponse();
        expectedResponse.setFirstName("John");
        expectedResponse.setLastName("Doe");
        expectedResponse.setActive(true);

        when(trainerService.updateTrainer(username, request)).thenReturn(expectedResponse);

        ResponseEntity<TrainerProfileResponse> response = trainerController.updateTrainerProfile(username, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse.getFirstName(), response.getBody().getFirstName());
        assertEquals(expectedResponse.getLastName(), response.getBody().getLastName());

        verify(trainerService).updateTrainer(username, request);
    }

    @Test
    public void updateTrainerProfile_ShouldThrowNotFound() {
        String username = "john.doe";
        TrainerProfileUpdateRequest request = new TrainerProfileUpdateRequest();
        request.setUsername(username);

        when(trainerService.updateTrainer(username, request)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> trainerController.updateTrainerProfile(username, request));
    }

    @Test
    public void updateTrainerProfile_ShouldThrowBadRequest() {
        String username = "john.doe";
        TrainerProfileUpdateRequest request = new TrainerProfileUpdateRequest();

        assertThrows(BadRequestException.class, () -> trainerController.updateTrainerProfile(username, request));
    }

    @Test
    public void getUnassignedTrainers_ShouldReturnUnassignedTrainers() {
        String username = "john.doe";
        List<TrainerShortProfile>  expectedResponse = List.of(new TrainerShortProfile(),  new TrainerShortProfile());

        when(trainerService.getUnassignedTrainers(username)).thenReturn(expectedResponse);

        ResponseEntity<List<TrainerShortProfile>> response = trainerController.getUnassignedTrainers(username);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void getUnassignedTrainers_ShouldThrowNotFound() {
        String username = "john.doe";

        when(trainerService.getUnassignedTrainers(username)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class ,() -> trainerController.getUnassignedTrainers(username));
    }

    @Test
    public void getTrainerTrainings_ShouldReturnTrainerTrainings() {
        String username = "john.doe";
        List<TrainerTrainingsResponse> expectedResponse = List.of(new TrainerTrainingsResponse());
        TrainerTrainingsRequest request = new TrainerTrainingsRequest();

        when(trainerService.getTrainerTrainings(username, request)).thenReturn(expectedResponse);

        ResponseEntity<List<TrainerTrainingsResponse>> response = trainerController.getTrainerTrainings(username, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), expectedResponse);

        verify(trainerService).getTrainerTrainings(username, request);
    }

    @Test
    public void getTraineeTrainings_ShouldThrowNotFound() {
        String username = "john.doe";
        TrainerTrainingsRequest request = new TrainerTrainingsRequest();

        when(trainerService.getTrainerTrainings(username, request)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> trainerController.getTrainerTrainings(username, request));
    }

    @Test
    public void updateActiveStatus_ShouldUpdate() {
        String username = "john.doe";

        ResponseEntity<String> response = trainerController.setTrainerActiveStatus(username, new ActivationRequest(true));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(trainerService).setTrainerActiveStatus(username, true);
    }

    @Test
    public void updateActiveStatus_ShouldThrowNotFound() {
        String username = "john.doe";

        doThrow(NotFoundException.class).when(trainerService).setTrainerActiveStatus(username, true);

        assertThrows(NotFoundException.class, () -> trainerController.setTrainerActiveStatus(username, new ActivationRequest(true)));
    }
}

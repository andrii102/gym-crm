package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainings.NewTrainingRequest;
import com.dre.gymapp.dto.trainings.TrainingTypeResponse;
import com.dre.gymapp.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {
    @InjectMocks
    private TrainingController trainingController;
    @Mock
    private TrainingService trainingService;

    @Test
    public void addTraining_ShouldAddTraining() {
        NewTrainingRequest request = new NewTrainingRequest();
        request.setTrainerUsername("john.doe");
        request.setTraineeUsername("john.doe1");

        ResponseEntity<String> response = trainingController.addTraining(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Training added successfully", response.getBody());

        verify(trainingService).createTraining(request);
    }

    @Test
    void getTrainingTypes_ShouldReturnListOfTypes() {
        List<TrainingTypeResponse> mockTypes = List.of(new TrainingTypeResponse(), new TrainingTypeResponse());
        when(trainingService.getAllTrainingTypes()).thenReturn(mockTypes);

        ResponseEntity<List<TrainingTypeResponse>> response = trainingController.getTrainingTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTypes, response.getBody());

        verify(trainingService).getAllTrainingTypes();
    }
}


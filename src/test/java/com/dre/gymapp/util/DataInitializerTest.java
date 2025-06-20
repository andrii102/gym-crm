package com.dre.gymapp.util;

import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DataInitializerTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    public void loadTraineesFromExistingFile(){
        dataInitializer.setTraineesFilePath("test-trainees.csv");
        dataInitializer.loadTrainees();

        verify(traineeService).createTrainee(
                argThat(t -> t.getUserId().equals("T0") &&
                        t.getFirstName().equals("John") &&
                        t.getLastName().equals("Doe"))
        );
    }

    @Test
    public void loadTrainersFromExistingFile(){
        dataInitializer.setTrainersFilePath("test-trainers.csv");
        dataInitializer.loadTrainers();

        verify(trainerService).createTrainer(
                argThat(t -> t.getUserId().equals("TR2") &&
                        t.getFirstName().equals("Jane") &&
                        t.getLastName().equals("Smith"))
        );
    }

}

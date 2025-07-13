package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainee.TraineeProfileResponse;
import com.dre.gymapp.dto.trainee.TraineeProfileUpdateRequest;
import com.dre.gymapp.dto.trainee.UpdateTrainersListRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.dto.trainings.TraineeTrainingsRequest;
import com.dre.gymapp.dto.trainings.TraineeTrainingsResponse;
import com.dre.gymapp.dto.user.ActivationRequest;
import com.dre.gymapp.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@Tag(name = "Trainee", description = "Trainee API")
public class TraineeController {

    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Operation(summary = "Create a new trainee")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(@PathVariable("username") String username) {
        TraineeProfileResponse response = traineeService.getTraineeProfileByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a new trainer")
    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> updateTraineeProfile(@PathVariable("username") String username,
                                                                       @RequestBody @Valid TraineeProfileUpdateRequest request) {
        TraineeProfileResponse response = traineeService.updateTrainee(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK) ;
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.ok("Trainee deleted successfully");
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerShortProfile>> updateTraineeTrainersList(@PathVariable("username") String username,
                                                                               @RequestBody @Valid UpdateTrainersListRequest trainerUsernameList) {
        List<TrainerShortProfile> response = traineeService.updateTraineeTrainerList(username, trainerUsernameList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TraineeTrainingsResponse>> getTraineeTrainings(@PathVariable("username") String username,
                                                                             @ModelAttribute TraineeTrainingsRequest request) {
        List<TraineeTrainingsResponse> response = traineeService.getTraineeTrainings(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{username}/activation")
    public ResponseEntity<String> updateActiveStatus(@PathVariable("username") String username, @RequestBody @Valid ActivationRequest request) {
        traineeService.setTraineeActiveStatus(username, request.isActive());
        return ResponseEntity.ok("Trainee active status updated successfully");
    }

}

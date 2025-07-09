package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainee.TraineeProfileResponse;
import com.dre.gymapp.dto.trainee.TraineeProfileUpdateRequest;
import com.dre.gymapp.service.TraineeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(@PathVariable("username") String username) {
        TraineeProfileResponse response = traineeService.getTraineeProfileByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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

}

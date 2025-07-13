package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainings.NewTrainingRequest;
import com.dre.gymapp.dto.trainings.TrainingTypeResponse;
import com.dre.gymapp.service.TrainingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<String> addTraining(@RequestBody @Valid NewTrainingRequest request) {
        trainingService.createTraining(request);
        return ResponseEntity.ok("Training added successfully");
    }

    @GetMapping("/training-types")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> response = trainingService.getAllTrainingTypes();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

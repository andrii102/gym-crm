package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainings.NewTrainingRequest;
import com.dre.gymapp.service.TrainingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
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
}

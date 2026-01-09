package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainings.NewTrainingRequest;
import com.dre.gymapp.dto.trainings.TrainingResponse;
import com.dre.gymapp.dto.trainings.TrainingTypeResponse;
import com.dre.gymapp.dto.trainings.TrainingUpdateRequest;
import com.dre.gymapp.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@Tag(name = "Trainings", description = "Trainings API")
public class TrainingController {

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Operation(summary = "Create a training", description = "Creates a new training session based on the given data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid training request", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> addTraining(
            @Parameter(description = "Training request body") @RequestBody @Valid NewTrainingRequest request) {
        trainingService.createTraining(request);
        return ResponseEntity.ok("Training added successfully");
    }

    @Operation(summary = "Get training by ID", description = "Returns training details for the given training ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training details retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TrainingResponse> getTrainingById(@PathVariable("id") Long id) {
        TrainingResponse response = trainingService.getTrainingDetailsById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update training", description = "Updates existing training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training updated successfully"),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateTrainingStatus(
            @Parameter(description = "ID of the training to update") @PathVariable("id") Long id,
            @Parameter(description = "Request body with fields to update") @RequestBody TrainingUpdateRequest request) {
        trainingService.updateTrainingStatus(id, request);
        return ResponseEntity.ok("Training status updated successfully");
    }

    @Operation(summary = "Get all training types", description = "Returns a list of all training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training types retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainingTypeResponse.class)))
    })
    @GetMapping("/training-types")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> response = trainingService.getAllTrainingTypes();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

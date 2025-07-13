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
@RequestMapping("/api/trainees")
@Tag(name = "Trainee", description = "Trainee API")
public class TraineeController {

    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Operation(summary = "Get Trainee Profile", description = "Returns trainee profile by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns trainee profile",
                    content = @Content(schema = @Schema(implementation = TraineeProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Trainee doesn't exist", content = @Content)
    })
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(
            @Parameter(description = "Username of the trainee") @PathVariable("username") String username) {
        TraineeProfileResponse response = traineeService.getTraineeProfileByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Trainee Profile", description = "Updates trainee profile ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee profile updated",
                    content = @Content(schema = @Schema(implementation = TraineeProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> updateTraineeProfile(
            @Parameter(description = "Username of the trainee") @PathVariable("username") String username,
            @RequestBody @Valid TraineeProfileUpdateRequest request) {
        TraineeProfileResponse response = traineeService.updateTrainee(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK) ;
    }

    @Operation(summary = "Delete Trainee", description = "Deletes a trainee by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(
            @Parameter(description = "Username of the trainee") @PathVariable("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.ok("Trainee deleted successfully");
    }

    @Operation(summary = "Update Trainee's Trainer List", description = "Updates the list of trainers assigned to a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer list updated",
                    content = @Content(schema = @Schema(implementation = TrainerShortProfile.class))),
            @ApiResponse(responseCode = "404", description = "Trainee or trainers not found", content = @Content)
    })
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerShortProfile>> updateTraineeTrainersList(
            @Parameter(description = "Username of the trainee") @PathVariable("username") String username,
            @RequestBody @Valid UpdateTrainersListRequest trainerUsernameList) {
        List<TrainerShortProfile> response = traineeService.updateTraineeTrainerList(username, trainerUsernameList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get Trainee Trainings", description = "Fetches the list of trainings for a given trainee with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved",
                    content = @Content(schema = @Schema(implementation = TraineeTrainingsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TraineeTrainingsResponse>> getTraineeTrainings(
            @Parameter(description = "Username of the trainee") @PathVariable("username") String username,
            @Parameter(description = "Filtering options") @ModelAttribute TraineeTrainingsRequest request) {
        List<TraineeTrainingsResponse> response = traineeService.getTraineeTrainings(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Trainee Active Status", description = "Updates the active status of a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee active status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @PatchMapping("/{username}/activation")
    public ResponseEntity<String> updateActiveStatus(
            @Parameter(description = "Username of the trainee") @PathVariable("username") String username,
            @RequestBody @Valid ActivationRequest request) {
        traineeService.setTraineeActiveStatus(username, request.isActive());
        return ResponseEntity.ok("Trainee active status updated successfully");
    }

}

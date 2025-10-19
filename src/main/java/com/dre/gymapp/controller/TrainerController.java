package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainer.TrainerProfileResponse;
import com.dre.gymapp.dto.trainer.TrainerProfileUpdateRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.dto.trainings.TrainerTrainingsRequest;
import com.dre.gymapp.dto.trainings.TrainerTrainingsResponse;
import com.dre.gymapp.dto.user.ActivationRequest;
import com.dre.gymapp.exception.BadRequestException;
import com.dre.gymapp.service.TrainerService;
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
@RequestMapping("/api/trainers")
@Tag(name = "Trainer", description = "Trainer API")
public class TrainerController {

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Operation(summary = "Get Trainer Profile", description = "Returns trainer profile by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee profile retrieved",
                    content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Trainee doesn't exist", content = @Content)
    })
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(
            @Parameter(description = "Username of the trainer") @PathVariable("username") String username) {
        TrainerProfileResponse response = trainerService.getTrainerProfileByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Trainer Profile", description = "Updates trainer profile ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile updated",
                    content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> updateTrainerProfile(
            @Parameter(description = "Username of the trainer") @PathVariable("username") String username,
            @RequestBody @Valid TrainerProfileUpdateRequest request ) {
        if (!username.equals(request.getUsername())) {
            throw new BadRequestException("Username in the path and in the body must match");
        }
        TrainerProfileResponse response = trainerService.updateTrainer(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get unassigned trainers", description = "Returns a list of trainers that are not assigned to a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unassigned trainers retrieved",
                    content = @Content(schema = @Schema(implementation = TrainerShortProfile.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TrainerShortProfile>> getUnassignedTrainers(
            @Parameter(description = "Username of the trainee") @PathVariable("username") String username) {
        List<TrainerShortProfile> trainers = trainerService.getUnassignedTrainers(username);
        return new ResponseEntity<>(trainers, HttpStatus.OK) ;
    }

    @Operation(summary = "Get Trainer's Trainings", description = "Returns trainer's trainings list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved",
                    content = @Content(schema = @Schema(implementation = TrainerTrainingsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainerTrainingsResponse>> getTrainerTrainings(
            @Parameter(description = "Username of the trainer") @PathVariable("username") String username,
            @Parameter(description = "Filtering options") @ModelAttribute TrainerTrainingsRequest request ) {
        List<TrainerTrainingsResponse> response = trainerService.getTrainerTrainings(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Set Trainer Active Status", description = "Activates or deactivates a trainer's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    @PatchMapping("/{username}/activation")
    public ResponseEntity<String> setTrainerActiveStatus(
            @Parameter(description = "Username of the trainer") @PathVariable("username") String username,
                                                          @RequestBody @Valid ActivationRequest request ) {
        trainerService.setTrainerActiveStatus(username, request.isActive());
        return ResponseEntity.ok("Trainer active status updated successfully");
    }

}

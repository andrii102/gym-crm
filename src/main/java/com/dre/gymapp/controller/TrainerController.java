package com.dre.gymapp.controller;

import com.dre.gymapp.dto.trainer.TrainerProfileResponse;
import com.dre.gymapp.dto.trainer.TrainerProfileUpdateRequest;
import com.dre.gymapp.dto.trainer.TrainerShortProfile;
import com.dre.gymapp.dto.trainings.TrainerTrainingsRequest;
import com.dre.gymapp.dto.trainings.TrainerTrainingsResponse;
import com.dre.gymapp.dto.user.ActivationRequest;
import com.dre.gymapp.exception.BadRequestException;
import com.dre.gymapp.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(@PathVariable("username") String username) {
        TrainerProfileResponse response = trainerService.getTrainerProfileByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> updateTrainerProfile(@PathVariable("username") String username,
                                                                       @RequestBody @Valid TrainerProfileUpdateRequest request ) {
        if (!username.equals(request.getUsername())) {
            throw new BadRequestException("Username in the path and in the body must match");
        }
        TrainerProfileResponse response = trainerService.updateTrainer(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TrainerShortProfile>> getUnassignedTrainers(@PathVariable("username") String username) {
        List<TrainerShortProfile> trainers = trainerService.getUnassignedTrainers(username);
        return new ResponseEntity<>(trainers, HttpStatus.OK) ;
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainerTrainingsResponse>> getTrainerTrainings(@PathVariable("username") String username,
                                                                              @ModelAttribute TrainerTrainingsRequest request ) {
        List<TrainerTrainingsResponse> response = trainerService.getTrainerTrainings(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{username}/activation")
    public ResponseEntity<String> setTrainerActiveStatus(@PathVariable("username") String username, @RequestBody @Valid ActivationRequest request ) {
        trainerService.setTrainerActiveStatus(username, request.isActive());
        return ResponseEntity.ok("Trainer active status updated successfully");
    }

}

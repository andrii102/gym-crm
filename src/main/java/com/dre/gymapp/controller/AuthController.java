package com.dre.gymapp.controller;

import com.dre.gymapp.dto.auth.LoginChangeRequest;
import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TraineeRegistrationRequest;
import com.dre.gymapp.dto.auth.TrainerRegistrationRequest;
import com.dre.gymapp.model.User;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import com.dre.gymapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private TraineeService traineeService;
    private TrainerService trainerService;

    @Autowired
    public AuthController(UserService userService, TraineeService traineeService, TrainerService trainerService) {
        this.userService = userService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @PostMapping("/trainee")
    public ResponseEntity<RegistrationResponse> registerTrainee(@RequestBody @Valid TraineeRegistrationRequest request ) {
        RegistrationResponse response = traineeService.createTrainee(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response) ;
    }

    @PostMapping("/trainer")
    public ResponseEntity<RegistrationResponse> registerTrainer(@RequestBody @Valid TrainerRegistrationRequest request ) {
        RegistrationResponse response = trainerService.createTrainer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response) ;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        userService.authenticateUser(username, password);
        return ResponseEntity.ok("Login Successful");
    }

    @PutMapping("/login")
    public ResponseEntity<String> changeLogin(@RequestBody @Valid LoginChangeRequest request ) {
        User user = userService.authenticateUser(request.getUsername(), request.getOldPassword());

        userService.changePassword(user, request.getNewPassword());

        return ResponseEntity.ok("Login changed successfully");
    }

}

package com.dre.gymapp.controller;

import com.dre.gymapp.dto.auth.*;
import com.dre.gymapp.service.AuthenticationService;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import com.dre.gymapp.service.UserService;
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

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and user registration API")
public class AuthController {

    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(UserService userService, TraineeService traineeService, TrainerService trainerService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register a new trainee", description = "Creates a new user and trainee profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee registered successfully",
                    content = @Content(schema = @Schema(implementation = RegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/trainee")
    public ResponseEntity<RegistrationResponse> registerTrainee(@RequestBody @Valid TraineeRegistrationRequest request ) {
        RegistrationResponse response = traineeService.createTrainee(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response) ;
    }

    @Operation(summary = "Register a new trainer", description = "Creates a new user and trainer profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer registered successfully",
                    content = @Content(schema = @Schema(implementation = RegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/trainer")
    public ResponseEntity<RegistrationResponse> registerTrainer(@RequestBody @Valid TrainerRegistrationRequest request ) {
        RegistrationResponse response = trainerService.createTrainer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response) ;
    }

    @Operation(summary = "Authenticate user", description = "Checks if the provided credentials are valid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@Parameter(description = "User's username") @RequestParam("username") String username,
                                        @Parameter(description = "User's password") @RequestParam("password") String password) {
        LoginResponse response = authenticationService.authenticate(username, password);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Change user password", description = "Authenticates the user and updates the password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid current credentials", content = @Content)
    })
    @PutMapping("/login")
    public ResponseEntity<String> changeLogin(@RequestBody @Valid LoginChangeRequest request ) {
        authenticationService.authenticate(request.getUsername(), request.getOldPassword());

        userService.changePassword(request.getUsername(), request.getNewPassword());

        return ResponseEntity.ok("Login changed successfully");
    }

    @Operation(summary = "Refresh access token", description = "Refreshes the access token using the refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        LoginResponse response = authenticationService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout", description = "Invalidates the refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token", content = @Content)
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request) {
        authenticationService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logout successful");
    }

}

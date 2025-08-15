package com.dre.gymapp.controller;

import com.dre.gymapp.dto.auth.*;
import com.dre.gymapp.service.AuthenticationService;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import com.dre.gymapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and user registration API")
public class AuthController {

    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthenticationService authenticationService;

    @Value("${jwt.refresh-expiration}")
    private Duration refreshExpiration;

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
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Parameter(description = "User's credentials for authentication")
                                                   @RequestBody LoginRequest request) {
        LoginResult loginResult = authenticationService.authenticate(request.getUsername(), request.getPassword());
        ResponseCookie cookie = ResponseCookie.from("refresh_token", loginResult.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh")
                .maxAge(refreshExpiration.getSeconds())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(loginResult.getAccessToken()));
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
    public ResponseEntity<LoginResponse> refresh(@Parameter(
            name = "refresh_token",
            description = "Refresh token stored in HttpOnly cookie",
            in = ParameterIn.COOKIE,
            required = true
    ) @CookieValue("refresh_token") String refreshToken) {
        LoginResult result = authenticationService.refreshToken(refreshToken);
        ResponseCookie cookie = ResponseCookie.from("refresh_token" , result.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh")
                .maxAge(refreshExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(result.getAccessToken()));
    }

    @Operation(summary = "Logout", description = "Invalidates the refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token", content = @Content)
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Parameter(
            name = "refresh_token",
            description = "Refresh token stored in HttpOnly cookie",
            in = ParameterIn.COOKIE,
            required = true
    ) @CookieValue("refresh_token") String refreshToken) {
        authenticationService.logout(refreshToken);
        return ResponseEntity.ok("Logout successful");
    }

}

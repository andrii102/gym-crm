package com.dre.gymapp.controller;

import com.dre.gymapp.dto.auth.LoginChangeRequest;
import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TraineeRegistrationRequest;
import com.dre.gymapp.dto.auth.TrainerRegistrationRequest;
import com.dre.gymapp.service.AuthenticationService;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import com.dre.gymapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;

    @Test
    public void registerTrainee_ShouldCreateTrainee(){
        TraineeRegistrationRequest request = new TraineeRegistrationRequest("John", "Doe");
        RegistrationResponse expectedResponse = new RegistrationResponse("john.doe", "password");

        when(traineeService.createTrainee(request)).thenReturn(expectedResponse);

        RegistrationResponse response = authController.registerTrainee(request).getBody();

        assertNotNull(response);
        assertEquals(expectedResponse.getUsername(), response.getUsername());
        assertEquals(expectedResponse.getPassword(), response.getPassword());
    }

    @Test
    public void registerTrainer_ShouldCreateTrainer(){
        TrainerRegistrationRequest request = new TrainerRegistrationRequest("John", "Doe", 1L);
        RegistrationResponse expectedResponse = new RegistrationResponse("john.doe", "password");

        when(trainerService.createTrainer(request)).thenReturn(expectedResponse);

        RegistrationResponse response = authController.registerTrainer(request).getBody();

        assertNotNull(response);
        assertEquals(expectedResponse.getUsername(), response.getUsername());
        assertEquals(expectedResponse.getPassword(), response.getPassword());
    }


    @Test
    public void login_ShouldLogin(){
        String username = "john.doe";
        String password = "password";

        ResponseEntity<String> response = authController.login(username, password);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(authenticationService).authenticate(username, password);
    }

    @Test
    public void login_ShouldNotLogin(){
        String username = "john.doe";
        String password = "password";

        when(authenticationService.authenticate(username, password)).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> authController.login(username, password));
    }

    @Test
    public void changeLogin_ShouldChangeLogin(){
        String username = "john.doe";
        LoginChangeRequest request = new LoginChangeRequest(username, "oldPassword",  "newPassword");
        Authentication mockAuth = mock(Authentication.class);

        when(authenticationService.authenticate("john.doe", "oldPassword")).thenReturn(mockAuth);

        ResponseEntity<String> response = authController.changeLogin(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService).changePassword(username, "newPassword");
    }

    @Test
    public void changeLogin_ShouldNotChangeLogin(){
        LoginChangeRequest request = new LoginChangeRequest("john.doe", "oldPassword",  "newPassword");

        when(authenticationService.authenticate("john.doe", "oldPassword")).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class,() -> authController.changeLogin(request));
    }
}


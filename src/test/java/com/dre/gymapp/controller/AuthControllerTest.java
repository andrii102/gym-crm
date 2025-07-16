package com.dre.gymapp.controller;

import com.dre.gymapp.dto.auth.LoginChangeRequest;
import com.dre.gymapp.dto.auth.RegistrationResponse;
import com.dre.gymapp.dto.auth.TraineeRegistrationRequest;
import com.dre.gymapp.dto.auth.TrainerRegistrationRequest;
import com.dre.gymapp.exception.UnauthorizedException;
import com.dre.gymapp.model.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private UserService userService;
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

        verify(userService).authenticateUser(username, password);
    }

    @Test
    public void login_ShouldNotLogin(){
        String username = "john.doe";
        String password = "password";

        when(userService.authenticateUser(username, password)).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class, () -> authController.login(username, password));
    }

    @Test
    public void changeLogin_ShouldChangeLogin(){
        LoginChangeRequest request = new LoginChangeRequest("john.doe", "oldPassword",  "newPassword");
        User user = new User("john.doe", "password");

        when(userService.authenticateUser("john.doe", "oldPassword")).thenReturn(user);

        ResponseEntity<String> response = authController.changeLogin(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService).changePassword(user, "newPassword");
    }

    @Test
    public void changeLogin_ShouldNotChangeLogin(){
        LoginChangeRequest request = new LoginChangeRequest("john.doe", "oldPassword",  "newPassword");

        when(userService.authenticateUser("john.doe", "oldPassword")).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class,() -> authController.changeLogin(request));

    }
}


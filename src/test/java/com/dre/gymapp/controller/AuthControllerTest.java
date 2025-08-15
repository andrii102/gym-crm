package com.dre.gymapp.controller;

import com.dre.gymapp.dto.auth.*;
import com.dre.gymapp.service.AuthenticationService;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import com.dre.gymapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import java.lang.reflect.Field;
import java.time.Duration;

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

    @BeforeEach
    void setUp() throws Exception {
        Field field = AuthController.class.getDeclaredField("refreshExpiration");
        field.setAccessible(true);
        field.set(authController, Duration.ofMinutes(15));
    }

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
    public void registerTrainer_ShouldCreateTrainer() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest("John", "Doe", 1L);
        RegistrationResponse expectedResponse = new RegistrationResponse("john.doe", "password");

        when(trainerService.createTrainer(request)).thenReturn(expectedResponse);

        RegistrationResponse response = authController.registerTrainer(request).getBody();

        assertNotNull(response);
        assertEquals(expectedResponse.getUsername(), response.getUsername());
        assertEquals(expectedResponse.getPassword(), response.getPassword());
    }


    @Test
    public void login_ShouldLogin() {
        String username = "john.doe";
        String password = "password";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(authenticationService.authenticate(username, password))
                .thenReturn(new LoginResult(accessToken, refreshToken));

        ResponseEntity<LoginResponse> response = authController.login(new LoginRequest(username, password));

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(accessToken, response.getBody().getAccessToken());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(authenticationService).authenticate(username, password);
    }

    @Test
    public void login_ShouldNotLogin(){
        String username = "john.doe";
        String password = "password";

        when(authenticationService.authenticate(username, password)).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> authController.login(new LoginRequest(username, password)));
    }

    @Test
    public void changeLogin_ShouldChangeLogin(){
        String username = "john.doe";
        LoginChangeRequest request = new LoginChangeRequest(username, "oldPassword",  "newPassword");
        LoginResult mockAuth = mock(LoginResult.class);

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

    @Test
    public void refresh_ShouldReturnNoContent_AndSetCookie() {
        String refreshToken = "refreshToken";

        LoginResult result = new LoginResult("accessToken", refreshToken);
        when(authenticationService.refreshToken(refreshToken)).thenReturn(result);

        ResponseEntity<LoginResponse> response = authController.refresh(refreshToken);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String setCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(setCookie);
        assertTrue(setCookie.contains("refresh_token=" + refreshToken));
        assertTrue(setCookie.contains("HttpOnly"));
    }

    @Test
    public void logout_ShouldLogout() {
        String refreshToken = "refreshToken";

        ResponseEntity<String> response = authController.logout(refreshToken);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(authenticationService).logout(refreshToken);
    }

}


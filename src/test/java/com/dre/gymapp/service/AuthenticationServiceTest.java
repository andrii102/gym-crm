package com.dre.gymapp.service;

import com.dre.gymapp.dto.auth.LoginResult;
import com.dre.gymapp.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private RefreshTokenService refreshTokenService;

    private static final String username = "john.doe";
    private static final String password = "password";
    private static final String jwt = "jwt";
    private static final String refreshToken = "refreshToken";
    private static final Duration refreshExpiration = Duration.ofMinutes(15);
    
    @BeforeEach
    void setUp() throws Exception {
        Field field = AuthenticationService.class.getDeclaredField("refreshExpiration");
        field.setAccessible(true);
        field.set(authenticationService, Duration.ofMinutes(15));
    }

    @Test
    public void authenticate_shouldAuthenticate() {
        Authentication authentication = mock(Authentication.class);

        when(loginAttemptService.isBlocked(eq(username))).thenReturn(false);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))).thenReturn(authentication);
        doNothing().when(loginAttemptService).loginSucceeded(username);
        when(jwtUtil.generateToken(username)).thenReturn(jwt);
        when(jwtUtil.generateRefreshToken(username)).thenReturn(refreshToken);
        doNothing().when(refreshTokenService).storeRefreshToken(username, refreshToken, refreshExpiration.toMillis());

        LoginResult response = authenticationService.authenticate(username, password);

        assertEquals(jwt, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        verify(loginAttemptService).loginSucceeded(username);
        verify(refreshTokenService).storeRefreshToken(username, refreshToken, refreshExpiration.toMillis());
        verify(jwtUtil).generateToken(username);
        verify(jwtUtil).generateRefreshToken(username);
        verifyNoMoreInteractions(loginAttemptService, refreshTokenService, jwtUtil, authenticationManager);
    }

    @Test
    public void authenticate_badCredentials_shouldFailAuthentication(){
        when(loginAttemptService.isBlocked(eq(username))).thenReturn(false);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
                .thenThrow(BadCredentialsException.class);
        doNothing().when(loginAttemptService).loginFailed(username);

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(username, password));

        verify(loginAttemptService).isBlocked(username);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        verify(loginAttemptService).loginFailed(username);
    }
    
    @Test
    public void authenticate_blockedUser_shouldThrowException(){
        when(loginAttemptService.isBlocked(username)).thenReturn(true);
        when(loginAttemptService.getRemainingBlockDuration(username)).thenReturn(Duration.ofSeconds(60));
        
        assertThrows(LockedException.class, () -> authenticationService.authenticate(username, password));

        verify(loginAttemptService).isBlocked(username);
        verify(loginAttemptService).getRemainingBlockDuration(username);
    }
    
    @Test
    public void refreshToken_shouldRefresh() {
        String newRefreshToken = "newRefreshToken";
        String newJwt = "newJwt";
        
        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
        when(jwtUtil.extractUsername(refreshToken)).thenReturn(username);
        when(refreshTokenService.isValid(username, refreshToken)).thenReturn(true);
        when(jwtUtil.generateToken(username)).thenReturn(newJwt);
        when(jwtUtil.generateRefreshToken(username)).thenReturn(newRefreshToken);
        doNothing().when(refreshTokenService).storeRefreshToken(username, newRefreshToken, refreshExpiration.toMillis());
        
        LoginResult response = authenticationService.refreshToken(refreshToken);
        
        assertEquals(newJwt, response.getAccessToken());
        assertEquals(newRefreshToken, response.getRefreshToken());
        
        verify(jwtUtil).validateToken(refreshToken);
        verify(jwtUtil).extractUsername(refreshToken);
        verify(refreshTokenService).isValid(username, refreshToken);
        verify(jwtUtil).generateToken(username);
        verify(jwtUtil).generateRefreshToken(username);
        verify(refreshTokenService).storeRefreshToken(username, newRefreshToken, refreshExpiration.toMillis());
        verifyNoMoreInteractions(jwtUtil, refreshTokenService);
    }

    @Test
    public void refreshToken_invalidToken_shouldThrowException() {
        when(jwtUtil.validateToken(refreshToken)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authenticationService.refreshToken(refreshToken));

        verify(jwtUtil).validateToken(refreshToken);
        verify(jwtUtil).extractUsername(refreshToken);
        verifyNoMoreInteractions(jwtUtil);
    }

    @Test
    public void logout_shouldDeleteRefreshToken() {
        when(jwtUtil.extractUsername(refreshToken)).thenReturn(username);
        when(refreshTokenService.isValid(username, refreshToken)).thenReturn(true);
        doNothing().when(refreshTokenService).deleteRefreshToken(username);

        authenticationService.logout(refreshToken);

        verify(jwtUtil).extractUsername(refreshToken);
        verify(refreshTokenService).isValid(username, refreshToken);
        verify(refreshTokenService).deleteRefreshToken(username);
        verifyNoMoreInteractions(jwtUtil, refreshTokenService);
    }
    
    
}

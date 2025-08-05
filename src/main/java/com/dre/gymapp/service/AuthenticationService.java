package com.dre.gymapp.service;

import com.dre.gymapp.dto.auth.LoginResponse;
import com.dre.gymapp.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.refresh-expiration}")
    private Duration refreshExpiration;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, LoginAttemptService loginAttemptService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
        this.refreshTokenService = refreshTokenService;
    }

    // Authenticates user with username and password
    public LoginResponse authenticate(String username, String password) {
        logger.info("Authenticating user with username: {}", username);
        if (loginAttemptService.isBlocked(username)) {
            Duration remaining = loginAttemptService.getRemainingBlockDuration(username);
            long minutes = (long) Math.ceil(remaining.toMillis() / 60000.0);
            throw new LockedException("Account is locked for " + minutes + " minute(s).");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            loginAttemptService.loginSucceeded(username);
            String jwt = jwtUtil.generateToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);
            refreshTokenService.storeRefreshToken(username, refreshToken, refreshExpiration.toMillis());
            return new LoginResponse(jwt, refreshToken);
        } catch(AuthenticationException ex) {
            logger.warn("Authentication failed for user: {}", username);
            loginAttemptService.loginFailed(username);
            throw ex;
        }
    }

    // Refreshes access token
    public LoginResponse refreshToken(String refreshToken) {
        logger.info("Refreshing token");
        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            if (refreshTokenService.isValid(username, refreshToken)) {
                String newJwt = jwtUtil.generateToken(username);
                String newRefreshToken = jwtUtil.generateRefreshToken(username);
                refreshTokenService.storeRefreshToken(username, newRefreshToken, refreshExpiration.toMillis());
                return new LoginResponse(newJwt, newRefreshToken);
            }
        }
        logger.warn("Invalid refresh token for user: {}", jwtUtil.extractUsername(refreshToken));
        throw new BadCredentialsException("Invalid refresh token");
    }

    // Logout method
    public void logout(String refreshToken) {
        logger.info("Logging out user with refresh token: {}", refreshToken);
        String username = jwtUtil.extractUsername(refreshToken);
        if (refreshTokenService.isValid(username, refreshToken)) {
            refreshTokenService.deleteRefreshToken(username);
        }
    }
}

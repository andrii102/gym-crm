package com.dre.gymapp.service;

import com.dre.gymapp.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
    }

    // Authenticates user with username and password
    public String authenticate(String username, String password) {
        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Account is locked for " + loginAttemptService.getRemainingBlockDuration(username).toMinutes()+1
                    + " minutes. Please try again later.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            loginAttemptService.loginSucceeded(username);

            return jwtUtil.generateToken(username);
        } catch(AuthenticationException ex) {
            loginAttemptService.loginFailed(username);
            throw ex;
        }
    }
}

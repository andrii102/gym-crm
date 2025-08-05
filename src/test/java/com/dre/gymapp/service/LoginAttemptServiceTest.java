package com.dre.gymapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.mockito.Mockito.*;

public class LoginAttemptServiceTest {

    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private LoginAttemptService loginAttemptService;

    private static final String username = "john.doe";
    private static final String FAIL_KEY_PREFIX = "login:fail:";
    private static final String BLOCK_KEY_PREFIX = "login:block:";

    @BeforeEach
    public void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        loginAttemptService = new LoginAttemptService(redisTemplate);
    }

    @Test
    public void loginSucceeded_ShouldDeleteLoginAttempt() {
        loginAttemptService.loginSucceeded(username);

        verify(redisTemplate).delete(FAIL_KEY_PREFIX + username);
        verify(redisTemplate).delete(BLOCK_KEY_PREFIX + username);
    }

    @Test
    public void loginFailed_ShouldIncrementLoginAttempt() {
        when(valueOperations.increment(FAIL_KEY_PREFIX + username, 1L)).thenReturn(1L);

        loginAttemptService.loginFailed(username);

        verify(valueOperations).increment(FAIL_KEY_PREFIX + username, 1L);
        verify(redisTemplate).expire(eq("login:fail:" + username), any(Duration.class));
        verify(valueOperations, never()).set(anyString(), anyString(), any(Duration.class));
        verify(redisTemplate, never()).delete("login:fail:" + username);
    }

    @Test
    public void loginFailed_maxAttemptsReached_ShouldSetBlockDuration() {
        when(valueOperations.increment(FAIL_KEY_PREFIX + username, 1L)).thenReturn(3L);

        loginAttemptService.loginFailed(username);

        verify(valueOperations).increment(FAIL_KEY_PREFIX + username, 1L);
        verify(valueOperations).set(eq(BLOCK_KEY_PREFIX + username), eq("true"), any(Duration.class));
        verify(redisTemplate).delete("login:fail:" + username);
        verify(redisTemplate, never()).expire(anyString(), any(Duration.class));
    }

    @Test
    public void isBlocked_ShouldReturnTrue() {
        when(redisTemplate.hasKey(BLOCK_KEY_PREFIX + username)).thenReturn(true);

        loginAttemptService.isBlocked(username);

        verify(redisTemplate).hasKey(BLOCK_KEY_PREFIX + username);
    }

    @Test
    public void isBlocked_ShouldReturnFalse() {
        when(redisTemplate.hasKey(BLOCK_KEY_PREFIX + username)).thenReturn(false);

        loginAttemptService.isBlocked(username);

        verify(redisTemplate).hasKey(BLOCK_KEY_PREFIX + username);
    }

    @Test
    public void getRemainingBlockDuration_ShouldReturnDuration() {
        when(redisTemplate.getExpire(BLOCK_KEY_PREFIX + username)).thenReturn(1000L);

        loginAttemptService.getRemainingBlockDuration(username);

        verify(redisTemplate).getExpire(BLOCK_KEY_PREFIX + username);
    }

}

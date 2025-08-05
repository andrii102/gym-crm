package com.dre.gymapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RefreshTokenServiceTest {
    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private RefreshTokenService refreshTokenService;

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String username = "john.doe";

    @BeforeEach
    public void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        refreshTokenService = new RefreshTokenService(redisTemplate);
    }

    @Test
    public void storeRefreshToken_shouldStoreToken() {
        refreshTokenService.storeRefreshToken(username, REFRESH_TOKEN, 1000L);
        verify(valueOperations).set(REFRESH_TOKEN_PREFIX + username,
                REFRESH_TOKEN, Duration.ofMillis(1000L));
    }

    @Test
    public void isValid_shouldReturnTrue() {
        when(valueOperations.get(REFRESH_TOKEN_PREFIX + username)).thenReturn(REFRESH_TOKEN);

        boolean result = refreshTokenService.isValid(username, REFRESH_TOKEN);

        assertTrue(result);
        verify(valueOperations).get(REFRESH_TOKEN_PREFIX + username);
    }

    @Test
    public void isValid_shouldReturnFalse() {
        when(valueOperations.get(REFRESH_TOKEN_PREFIX + username)).thenReturn(null);

        boolean result = refreshTokenService.isValid(username, REFRESH_TOKEN);

        assertFalse(result);
        verify(valueOperations).get(REFRESH_TOKEN_PREFIX + username);
    }

    @Test
    public void deleteRefreshToken_shouldDeleteToken() {
        refreshTokenService.deleteRefreshToken(username);
        verify(redisTemplate).delete(REFRESH_TOKEN_PREFIX + username);
    }
}

package com.dre.gymapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private final StringRedisTemplate redisTemplate;

    public RefreshTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeRefreshToken(String username, String refreshToken, long durationMillis) {
        logger.info("Storing refresh token for user '{}', expires in {} ms", username, durationMillis);
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + username, refreshToken, Duration.ofMillis(durationMillis));
    }

    public boolean isValid(String username, String refreshToken) {
        String storedRefreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + username);
        boolean isValid = storedRefreshToken != null && storedRefreshToken.equals(refreshToken);
        if (!isValid) {
            logger.warn("Invalid refresh token for user '{}'", username);
        }
        return isValid;
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + username);
        logger.info("Deleted refresh token for user '{}'", username);
    }

}

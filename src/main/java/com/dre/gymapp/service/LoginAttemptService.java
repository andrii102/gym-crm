package com.dre.gymapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginAttemptService {
    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);

    private static final String FAIL_KEY_PREFIX = "login:fail:";
    private static final String BLOCK_KEY_PREFIX = "login:block:";

    private static final int MAX_ATTEMPTS = 3;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(5);

    private final StringRedisTemplate redisTemplate;

    public LoginAttemptService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void loginSucceeded(String username) {
        logger.info("Login succeeded for user '{}'", username);
        redisTemplate.delete(FAIL_KEY_PREFIX + username);
        redisTemplate.delete(BLOCK_KEY_PREFIX + username);
    }

    public void loginFailed(String username) {
        logger.warn("Login failed for user '{}'", username);

        String failKey = FAIL_KEY_PREFIX + username;
        Long attempts = redisTemplate.opsForValue().increment(failKey, 1L);
        logger.debug("User '{}' has {} failed login attempt(s)", username, attempts);

        if (attempts == null || attempts == 1L) {
            redisTemplate.expire(failKey, BLOCK_DURATION);
        }

        if (attempts != null && attempts >= MAX_ATTEMPTS) {
            redisTemplate.opsForValue().set(BLOCK_KEY_PREFIX + username, "true", BLOCK_DURATION);
            redisTemplate.delete(failKey);
            logger.warn("User '{}' is blocked for {} minutes", username, BLOCK_DURATION.toMinutes());
        }
    }

    public boolean isBlocked(String username) {
        logger.debug("Checking if user '{}' is blocked", username);
        return redisTemplate.hasKey(BLOCK_KEY_PREFIX + username);
    }

    public Duration getRemainingBlockDuration(String username) {
        logger.debug("Getting remaining block duration for user '{}'", username);
        long blockDuration = redisTemplate.getExpire(BLOCK_KEY_PREFIX + username);
        if (blockDuration < 0) {
            return Duration.ZERO;
        }
        return Duration.ofSeconds(blockDuration);
    }
}

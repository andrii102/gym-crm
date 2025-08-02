package com.dre.gymapp.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginAttemptService {
    private static final String FAIL_KEY_PREFIX = "login:fail:";
    private static final String BLOCK_KEY_PREFIX = "login:block:";

    private static final int MAX_ATTEMPTS = 3;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(5);

    private final StringRedisTemplate redisTemplate;

    public LoginAttemptService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void loginSucceeded(String username) {
        redisTemplate.delete(FAIL_KEY_PREFIX + username);
        redisTemplate.delete(BLOCK_KEY_PREFIX + username);
    }

    public void loginFailed(String username) {
        String failKey = FAIL_KEY_PREFIX + username;
        Long attempts = redisTemplate.opsForValue().increment(failKey, 1L);

        if (attempts == null || attempts == 1L) {
            redisTemplate.expire(failKey, BLOCK_DURATION);
        }

        if (attempts != null && attempts >= MAX_ATTEMPTS) {
            redisTemplate.opsForValue().set(BLOCK_KEY_PREFIX + username, "true", BLOCK_DURATION);
            redisTemplate.delete(failKey);
        }
    }

    public boolean isBlocked(String username) {
        return redisTemplate.hasKey(BLOCK_KEY_PREFIX + username);
    }

    public Duration getRemainingBlockDuration(String username) {
        long blockDuration = redisTemplate.getExpire(BLOCK_KEY_PREFIX + username);
        if (blockDuration < 0) {
            return Duration.ZERO;
        }
        return Duration.ofSeconds(blockDuration);
    }
}

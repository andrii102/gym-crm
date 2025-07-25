package com.dre.gymapp.actuator;

import jakarta.persistence.EntityManager;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final EntityManager entityManager;

    public DatabaseHealthIndicator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Health health() {
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            return Health.up().withDetail("Database", "Query OK").build();
        } catch (Exception e) {
            return Health.down().withDetail("Database", "Unavailable").withException(e).build();
        }
    }
}

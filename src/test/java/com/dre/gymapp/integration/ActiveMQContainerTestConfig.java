package com.dre.gymapp.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.activemq.ArtemisContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class ActiveMQContainerTestConfig {

    @Container
    static ArtemisContainer artemis = new ArtemisContainer("apache/activemq-artemis:2.31.2-alpine")
            .withUser("artemis")
            .withPassword("artemis");

}



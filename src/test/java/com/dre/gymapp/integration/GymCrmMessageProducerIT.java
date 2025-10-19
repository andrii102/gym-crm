package com.dre.gymapp.integration;

import com.dre.gymapp.dto.trainings.TrainingEventRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(ActiveMQContainerTestConfig.class)
@ActiveProfiles("test")
class GymCrmMessageProducerIT {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Integration test: send a message to the queue using JmsTemplate
     * and verify it can be received (simulating a consumer).
     */
    @Test
    void shouldSendMessageToQueue() {
        TrainingEventRequest event = new TrainingEventRequest(
                "username1", "firstName1", "lastName1",
                true, LocalDate.of(2025, 1, 1), 10, "ADD");

        jmsTemplate.convertAndSend("trainings.queue", event);

        Object message = jmsTemplate.receiveAndConvert("trainings.queue");
        assertNotNull(message);
    }
}


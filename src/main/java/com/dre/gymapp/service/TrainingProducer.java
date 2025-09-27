package com.dre.gymapp.service;

import com.dre.gymapp.dto.trainings.TrainingEventRequest;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrainingProducer {
    private final JmsTemplate jmsTemplate;

    public TrainingProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(String destination, TrainingEventRequest payload) {
        jmsTemplate.convertAndSend(destination, payload,message -> {
            message.setStringProperty("trainingProducer", "training");
            String transactionId = MDC.get("transactionId");
            if (transactionId != null) {
                message.setStringProperty("transactionId", transactionId);
            }
            return message;
        });
    }
}

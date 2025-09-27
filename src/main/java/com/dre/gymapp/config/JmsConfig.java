package com.dre.gymapp.config;

import com.dre.gymapp.dto.trainings.TrainingEventRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;

@EnableJms
@Slf4j
@Configuration
public class JmsConfig {

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        HashMap<String, Class<?>> idClassMappings = new HashMap<>();
        idClassMappings.put("trainingEventRequest", TrainingEventRequest.class);
        converter.setTypeIdMappings(idClassMappings);
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer,
            MappingJackson2MessageConverter converter) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // allow Spring Boot auto config to set defaults (if present)
        configurer.configure(factory, connectionFactory);

        // exceptions cause rollback -> redelivery by broker
        factory.setSessionTransacted(true);
        factory.setMessageConverter(converter);
        factory.setErrorHandler(t -> log.error("Error in JMS listener: ", t));
        return factory;
    }

}

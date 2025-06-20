package com.dre.gymapp.config;

import com.dre.gymapp.model.Trainee;
import com.dre.gymapp.model.Trainer;
import com.dre.gymapp.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.dre.gymapp")
@PropertySource("classpath:application.properties")
public class AppConfig {
    // Bean for storing Trainee objects with user IDs as keys
    @Bean
    public Map<String, Trainee> traineeMap() {
        return new HashMap<>();
    }

    // Bean for storing Training objects with training names as keys
    @Bean
    public Map<String, Training> trainingMap() {
        return new HashMap<>();
    }

    // Bean for storing Trainer objects with user IDs as keys 
    @Bean
    public Map<String, Trainer> trainerMap() {
        return new HashMap<>();
    }
}


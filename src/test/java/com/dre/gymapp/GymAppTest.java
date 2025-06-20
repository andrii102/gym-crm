package com.dre.gymapp;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GymAppTest {

    @Test
    void contextLoadsSuccessfully() {
        ApplicationContext context = new AnnotationConfigApplicationContext(GymApp.class);
        assertNotNull(context, "The application context should not be null");
    }
}
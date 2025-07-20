package com.dre.gymapp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dre.gymapp")
public class GymApp {
    public static void main(String[] args) {
        SpringApplication.run(GymApp.class, args);
    }
}

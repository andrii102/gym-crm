package com.dre.gymapp.bdd;

// ... all your imports

import com.dre.gymapp.actuator.DatabaseHealthIndicator;
import com.dre.gymapp.dao.*;
import com.dre.gymapp.security.CustomUserDetailsService;
import com.dre.gymapp.service.TraineeService;
import com.dre.gymapp.service.TrainerService;
import com.dre.gymapp.service.TrainingService;
import com.dre.gymapp.service.UserService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class CucumberSpringConfig {
    @MockitoBean
    protected CustomUserDetailsService userDetailsService;
    @MockitoBean
    protected PasswordEncoder passwordEncoder;

    // Actuator Mock
    @MockitoBean
    protected DatabaseHealthIndicator databaseHealthIndicator;

    // --- Mock ALL Database Repositories/DAOs ---
    @MockitoBean
    protected UserDao userDao;
    @MockitoBean
    protected TrainerDao trainerDao;
    @MockitoBean
    protected TraineeDao traineeDao;
    @MockitoBean
    protected TrainingDao trainingDao;
    @MockitoBean
    protected TrainingTypeDao trainingTypeDao;

    @MockitoBean
    protected StringRedisTemplate stringRedisTemplate;

    @MockitoBean
    protected TraineeService traineeService;
    @MockitoBean
    protected TrainerService trainerService;
    @MockitoBean
    protected UserService userService;
    @MockitoBean
    protected TrainingService trainingService;

}
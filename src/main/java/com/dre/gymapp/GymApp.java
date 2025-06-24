package com.dre.gymapp;


import com.dre.gymapp.config.AppConfig;
import com.dre.gymapp.facade.GymFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GymApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        GymFacade gymFacade = context.getBean(GymFacade.class);
        gymFacade.printAllTrainees();
    }
}

package com.dre.gymapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TraineeController {

    @GetMapping("trainees")
    public ResponseEntity<String> getUsers() {
        return new ResponseEntity<>("Trainees", HttpStatus.OK);
    }
}

package com.dre.gymapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    String firstName;
    String lastName;
    String username;
    String password;
    boolean isActive;

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        isActive = true;
    }}

package com.dre.gymapp.dto.auth;

import lombok.Data;

@Data
public class RegistrationResponse {
    String username;
    String password;

    public RegistrationResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

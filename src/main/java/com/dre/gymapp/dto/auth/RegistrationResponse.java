package com.dre.gymapp.dto.auth;

import lombok.Data;

@Data
public class RegistrationResponse {
    private String username;
    private String password;

    public RegistrationResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

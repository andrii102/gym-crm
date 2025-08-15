package com.dre.gymapp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {
    String accessToken;
    String refreshToken;
}

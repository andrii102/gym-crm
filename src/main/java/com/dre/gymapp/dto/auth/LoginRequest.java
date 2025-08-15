package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for logging in a user")
public class LoginRequest {
    @Schema(description = "Username of the user logging in")
    private String username;
    @Schema(description = "Password of the user logging in")
    private String password;
}

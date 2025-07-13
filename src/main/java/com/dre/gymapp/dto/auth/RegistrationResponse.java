package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response object containing registration details")
public class RegistrationResponse {
    @Schema(description = "Generated username for the registered user")
    private String username;
    @Schema(description = "Generated password for the registered user")
    private String password;

    public RegistrationResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

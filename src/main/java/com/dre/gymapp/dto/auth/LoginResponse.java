package com.dre.gymapp.dto.auth;

import com.dre.gymapp.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response object containing login details")
public class LoginResponse {
    @Schema(description = "Access token for the logged in user")
    private String accessToken;
    @Schema(description = "Role of the logged in user")
    private Role role;
}

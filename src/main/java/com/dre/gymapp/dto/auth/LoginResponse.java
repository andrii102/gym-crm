package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing login details")
public class LoginResponse {
    @Schema(description = "JWT token for authentication")
    String jwt;

    @Schema(description = "JWT token for refreshing access tokens")
    String refreshToken;
}

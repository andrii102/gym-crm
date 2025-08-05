package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Request object for logging out a user")
public class LogoutRequest {
    @Schema(description = "Refresh token to invalidate during logout")
    @NotNull(message = "RefreshToken is required")
    private String refreshToken;
}

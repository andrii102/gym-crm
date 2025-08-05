package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Request object for refreshing JWT tokens")
public class TokenRefreshRequest {
    @Schema(description = "Refresh token")
    @NotBlank(message = "RefreshToken is required")
    private String refreshToken;
}

package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response object containing a new access token")
public class RefreshTokenResponse {
    @Schema(description = "New access token")
    String accessToken;
}

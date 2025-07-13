package com.dre.gymapp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for changing user login credentials")
public class LoginChangeRequest {
    @Schema(description = "Username of the user whose login credentials are being changed")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Current password of the user requesting the change")
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @Schema(description = "New password that will replace the current password")
    @NotBlank(message = "New password is required")
    private String newPassword;
}
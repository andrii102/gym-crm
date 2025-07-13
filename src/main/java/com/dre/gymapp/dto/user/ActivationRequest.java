package com.dre.gymapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for activating or deactivating a user")
public class ActivationRequest {
    @Schema(description = "Active status of the user")
    @NotNull(message = "isActive is required")
    private boolean isActive;
}

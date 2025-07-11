package com.dre.gymapp.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivationRequest {
    @NotNull(message = "isActive is required")
    private boolean isActive;
}

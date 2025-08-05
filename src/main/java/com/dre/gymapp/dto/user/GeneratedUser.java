package com.dre.gymapp.dto.user;

import com.dre.gymapp.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User object with raw password")
public class GeneratedUser {
    @Schema(description = "User object")
    private User user;
    @Schema(description = "Raw password")
    private String rawPassword;
}

package com.dre.gymapp.dto.user;

import com.dre.gymapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedUser {
    private User user;
    private String rawPassword;
}

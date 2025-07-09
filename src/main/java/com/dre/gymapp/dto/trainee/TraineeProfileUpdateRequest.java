package com.dre.gymapp.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileUpdateRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Firstname is required")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    private String address;

    private LocalDate dateOfBirth;

    @NotNull(message = "IsActive is required")
    private boolean isActive;

    public TraineeProfileUpdateRequest(String username, String firstName, String lastName, boolean isActive) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }
}

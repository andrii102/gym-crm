package com.dre.gymapp.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Update trainee's trainers list request")
public class UpdateTrainersListRequest {
    @Schema(description = "List of trainer usernames")
    @NotNull(message = "Trainers list is required")
    @Size(min = 1, message = "List cannot be empty")
    private List<@NotBlank(message = "Trainer username cannot be empty") String> trainers;
}

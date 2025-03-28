package org.example.highlighterdemo.model.requestDTO;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "userId is null") String userId,
        @NotBlank(message = "password is null") String password
) {
}

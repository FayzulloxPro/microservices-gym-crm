package dev.fayzullokh.dtos.auth;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;

public record TokenRequest(@NotBlank String username, @NotBlank String password) {
    public TokenRequest {

        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username must not be blank");
        }
    }
}

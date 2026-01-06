package com.inventory.auth.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "Username is required")
        @Pattern(
                regexp = "^(?!\\d).*[a-zA-Z].*$",
                message = "Username must not start with a number and must contain at least one letter"
        )
        String username,
        @NotBlank(message = "Password is required")
        String password) {

}

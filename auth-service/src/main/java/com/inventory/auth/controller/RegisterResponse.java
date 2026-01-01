package com.inventory.auth.controller;

public record RegisterResponse(
		Long userId,
        String username,
        String role,
        String message
) {}

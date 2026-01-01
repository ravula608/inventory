package com.inventory.auth.controller;

public record RegisterRequest(
        String username,
        String password,
        String role
) {}

package com.inventory.orderservice.exceptions;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String message,
        String path,
        LocalDateTime timestamp
) {}

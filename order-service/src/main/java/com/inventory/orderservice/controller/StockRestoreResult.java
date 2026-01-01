package com.inventory.orderservice.controller;

public record StockRestoreResult(
        boolean success,
        String message
) {}

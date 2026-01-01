package com.inventory.productservice.controller;

public record StockRestoreResult(
        boolean success,
        String message
) {}

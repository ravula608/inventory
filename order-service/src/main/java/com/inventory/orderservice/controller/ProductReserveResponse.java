package com.inventory.orderservice.controller;

public record ProductReserveResponse(
        Long productId,
        double price
) {}

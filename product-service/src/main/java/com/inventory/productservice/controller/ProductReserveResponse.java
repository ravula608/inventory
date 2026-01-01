package com.inventory.productservice.controller;

public record ProductReserveResponse(
        Long productId,
        double price
) {}

package com.inventory.orderservice.controller;

public record CreateOrderRequest(
        Long productId,
        int quantity
) {}

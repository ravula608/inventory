package com.inventory.productservice.controller;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProductModelTest {

    @Test
    void builder_and_accessors_and_toString() {
        LocalDateTime now = LocalDateTime.now();
        Product p = Product.builder()
                .productId(1L)
                .name("Test")
                .description("Desc")
                .price(10.0)
                .availableQuantity(5)
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, p.getProductId());
        assertEquals("Test", p.getName());
        assertEquals("Desc", p.getDescription());
        assertEquals(10.0, p.getPrice());
        assertEquals(5, p.getAvailableQuantity());
        assertTrue(p.isActive());
        assertNotNull(p.toString());

        p.setName("New");
        assertEquals("New", p.getName());
    }
}


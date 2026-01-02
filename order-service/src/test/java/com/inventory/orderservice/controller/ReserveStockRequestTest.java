package com.inventory.orderservice.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReserveStockRequestTest {

    @Test
    void record_accessors_work() {
        ReserveStockRequest req = new ReserveStockRequest(7);
        assertEquals(7, req.quantity());
        assertNotNull(req.toString());
    }
}


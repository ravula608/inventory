package com.inventory.orderservice.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductClientFallbackTest {

    @Test
    void restoreStock_returnsFailureResult() {
        ProductClientFallback fallback = new ProductClientFallback();

        RestoreStockRequest req = new RestoreStockRequest(5);
        StockRestoreResult res = fallback.restoreStock(10L, req);

        assertNotNull(res);
        assertFalse(res.success());
        assertTrue(res.message().contains("failed"));
    }
}


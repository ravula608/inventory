package com.inventory.orderservice.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionsTest {

    @Test
    void productStockReserveException_messageContainsProductId() {
        ProductStockReserveException ex = new ProductStockReserveException(42L);
        assertTrue(ex.getMessage().contains("42"));
    }

    @Test
    void productNotFoundAndOthers_haveConstructors() {
        ProductNotFoundException pn = new ProductNotFoundException(5L);
        assertTrue(pn.getMessage().contains("5"));

        InsufficientStocksException ise = new InsufficientStocksException("no stock");
        assertEquals("Insufficient stock for product: no stock", ise.getMessage());

        OrderAlreadyCancelledException oac = new OrderAlreadyCancelledException(9L);
        assertTrue(oac.getMessage().contains("9"));

        OrderNotFoundException onf = new OrderNotFoundException(8L);
        assertTrue(onf.getMessage().contains("8"));

        ProductNotFoundException pnf = new ProductNotFoundException(7L);
        assertTrue(pnf.getMessage().contains("7"));
    }
}


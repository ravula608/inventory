package com.inventory.orderservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductClient productClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    void reserveStock_invokesRestTemplate_andReturnsResponse() {
        ProductReserveResponse resp = new ProductReserveResponse(1L, 99.9);
        when(restTemplate.postForObject(
                eq("http://product-service/products/1/reserve"),
                org.mockito.ArgumentMatchers.any(ReserveStockRequest.class),
                eq(ProductReserveResponse.class)
        )).thenReturn(resp);

        ProductReserveResponse result = productClient.reserveStock(1L, 2);
        assertNotNull(result);
        assertEquals(1L, result.productId());
        assertEquals(99.9, result.price());
    }

    @Test
    void reserveStockFallback_throwsProductStockReserveException() {
        Exception cause = new RuntimeException("boom");
        try {
            productClient.reserveStockFallback(2L, 1, cause);
            fail("Expected ProductStockReserveException");
        } catch (Exception ex) {
            assertTrue(ex instanceof com.inventory.orderservice.exceptions.ProductStockReserveException);
            assertTrue(ex.getMessage().contains("2"));
        }
    }
}


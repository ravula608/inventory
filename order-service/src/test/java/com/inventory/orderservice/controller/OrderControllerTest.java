package com.inventory.orderservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController controller;

    @BeforeEach
    void setUp() {
        // MockitoExtension takes care of init
    }

    @Test
    void testGetOrderDetails() {
        when(orderService.getOrders()).thenReturn("orders-list");

        String result = controller.getOrderDetails();

        assertEquals("orders-list", result);
        verify(orderService).getOrders();
    }

    @Test
    void testGet() {
        String res = controller.get();
        assertEquals("Order from ...", res);
    }

    @Test
    void testCreateOrder() {
        CreateOrderRequest req = new CreateOrderRequest(10L, 2);
        Order created = Order.builder()
                .orderId(1L)
                .userId(5L)
                .productId(10L)
                .quantity(2)
                .status(OrderStatus.CONFIRMED)
                .totalPrice(200.0)
                .build();

        when(orderService.createOrder(eq(5L), any(CreateOrderRequest.class))).thenReturn(created);

        Order result = controller.createOrder(5L, req);

        assertEquals(created, result);
        verify(orderService).createOrder(eq(5L), any(CreateOrderRequest.class));
    }

    @Test
    void testCancelOrder() {
        StockRestoreResult restoreResult = new StockRestoreResult(true, "restored");
        when(orderService.cancelOrder(eq(1L), eq(5L))).thenReturn(restoreResult);

        StockRestoreResult result = controller.cancelOrder(1L, 5L, "USER");

        assertEquals(restoreResult, result);
        verify(orderService).cancelOrder(eq(1L), eq(5L));
    }
}


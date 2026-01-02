package com.inventory.orderservice.model;

import com.inventory.orderservice.controller.Order;
import com.inventory.orderservice.controller.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrderModelTest {

    @Test
    void builder_getters_setters_equals_hashcode_toString() {
        LocalDateTime now = LocalDateTime.now();

        Order order = Order.builder()
                .orderId(10L)
                .userId(5L)
                .productId(20L)
                .quantity(3)
                .status(OrderStatus.CONFIRMED)
                .totalPrice(150.5)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(10L, order.getOrderId());
        assertEquals(5L, order.getUserId());
        assertEquals(20L, order.getProductId());
        assertEquals(3, order.getQuantity());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(150.5, order.getTotalPrice());
        assertEquals(now, order.getCreatedAt());
        assertEquals(now, order.getUpdatedAt());

        // setters
        order.setOrderId(11L);
        order.setUserId(6L);
        order.setProductId(21L);
        order.setQuantity(4);
        order.setStatus(OrderStatus.CANCELLED);
        order.setTotalPrice(200.0);

        assertEquals(11L, order.getOrderId());
        assertEquals(6L, order.getUserId());
        assertEquals(21L, order.getProductId());
        assertEquals(4, order.getQuantity());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(200.0, order.getTotalPrice());

        // equals/hashCode
        Order same = order;
        assertEquals(order, same);
        assertEquals(order.hashCode(), same.hashCode());

        Order different = Order.builder()
                .orderId(999L)
                .userId(2L)
                .productId(3L)
                .quantity(1)
                .status(OrderStatus.CONFIRMED)
                .totalPrice(10.0)
                .build();

        assertNotEquals(order, different);

        // toString should not throw
        assertNotNull(order.toString());
    }
}


package com.inventory.orderservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.inventory.orderservice.exceptions.OrderAlreadyCancelledException;
import com.inventory.orderservice.exceptions.OrderNotFoundException;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ProductClient productClient;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductFeignClient productFeignClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetOrders() {
        when(restTemplate.getForObject("http://product-service/products", String.class))
                .thenReturn("products-list");

        String res = orderService.getOrders();
        assertEquals("products-list", res);
    }

    @Test
    void testCreateOrder_happyPath() {
        CreateOrderRequest req = new CreateOrderRequest(10L, 2);
        ProductReserveResponse reserveResponse = new ProductReserveResponse(10L, 50.0);

        when(productClient.reserveStock(eq(10L), eq(2))).thenReturn(reserveResponse);

        Order toSave = Order.builder()
                .userId(5L)
                .productId(10L)
                .quantity(2)
                .totalPrice(100.0)
                .status(OrderStatus.CONFIRMED)
                .build();

        Order saved = Order.builder()
                .orderId(1L)
                .userId(5L)
                .productId(10L)
                .quantity(2)
                .totalPrice(100.0)
                .status(OrderStatus.CONFIRMED)
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        Order result = orderService.createOrder(5L, req);

        assertEquals(saved, result);
        verify(productClient).reserveStock(eq(10L), eq(2));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCancelOrder_happyPath() {
        Order existing = Order.builder()
                .orderId(1L)
                .userId(5L)
                .productId(10L)
                .quantity(2)
                .status(OrderStatus.CONFIRMED)
                .build();

        when(orderRepository.findById(eq(1L))).thenReturn(Optional.of(existing));

        StockRestoreResult restoreResult = new StockRestoreResult(true, "restored");
        when(productFeignClient.restoreStock(eq(10L), any(RestoreStockRequest.class))).thenReturn(restoreResult);

        StockRestoreResult result = orderService.cancelOrder(1L, 5L);

        assertEquals(restoreResult, result);
        verify(orderRepository).findById(eq(1L));
        verify(productFeignClient).restoreStock(eq(10L), any(RestoreStockRequest.class));
    }

    @Test
    void testCancelOrder_orderNotFound() {
        when(orderRepository.findById(eq(99L))).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.cancelOrder(99L, 5L));
    }

    @Test
    void testCancelOrder_alreadyCancelled() {
        Order existing = Order.builder()
                .orderId(2L)
                .userId(5L)
                .productId(10L)
                .quantity(1)
                .status(OrderStatus.CANCELLED)
                .build();

        when(orderRepository.findById(eq(2L))).thenReturn(Optional.of(existing));

        assertThrows(OrderAlreadyCancelledException.class, () -> orderService.cancelOrder(2L, 5L));
    }
}


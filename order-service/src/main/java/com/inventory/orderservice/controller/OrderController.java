package com.inventory.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {
	
	@Value("${message}")
	String message;
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping()
	public String getOrderDetails() {
		return orderService.getOrders();
	}
	
	@GetMapping("/get")
	public String get() {
		return "Order from ...";
	}

	@PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CreateOrderRequest request) {
		log.info("Create order started");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(userId, request));
    }
	
	@PostMapping("/{orderId}/cancel")
    public StockRestoreResult cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Role", required = false) String role
    ) {

        log.info("cancelOrder called: orderId={}, X-User-Id={}, X-User-Role={}", orderId, userId, role);
        return orderService.cancelOrder(orderId, userId);
    }

}

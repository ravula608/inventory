package com.inventory.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.inventory.orderservice.exceptions.OrderAlreadyCancelledException;
import com.inventory.orderservice.exceptions.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    private ProductClient productClient;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductFeignClient productFeignClient;

	public String getOrders() {
		String s = restTemplate.getForObject("http://product-service/products", String.class);
		return s;
		
	}

	@Transactional
    public Order createOrder(Long userId, CreateOrderRequest request) {
		log.info("Create order started");

        // Reserve stock
        ProductReserveResponse response =
                productClient.reserveStock(
                        request.productId(),
                        request.quantity()
                );

        // Create order
        Order order = Order.builder()
        		.userId(userId)
        		.productId(request.productId())
        		.quantity(request.quantity())
        		.totalPrice(response.price() * request.quantity())
        		.status(OrderStatus.CONFIRMED)
        		.build();
        Order createdOrder = orderRepository.save(order);
        log.info("Order created");
        
        return createdOrder;
    }
	
	@Transactional
    public StockRestoreResult cancelOrder(Long orderId, Long userId) {
		log.info("Cancelling order for orderId: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(orderId));

        // Authorization
        /*if (!isAdmin && !order.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }*/

        if (order.getStatus() == OrderStatus.CANCELLED) {
        	throw new OrderAlreadyCancelledException(orderId);
        }
        
        RestoreStockRequest restoreStockRequest = new RestoreStockRequest(order.getQuantity());

        // Restore stock
        StockRestoreResult restoreResult = productFeignClient.restoreStock(
                order.getProductId(),
                restoreStockRequest
        );

        // Update order
        if (restoreResult.success()) {
        	order.setStatus(OrderStatus.CANCELLED);
        	log.info("Order cancelled");
		} else {
			log.error("Failed to restore stock for orderId: {}", orderId);
		}
        return restoreResult;
    }

}

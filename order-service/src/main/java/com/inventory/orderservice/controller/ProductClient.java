package com.inventory.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.inventory.orderservice.exceptions.ProductStockReserveException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductClient {

	@Autowired
    private RestTemplate restTemplate;

	@CircuitBreaker(name = "product-service", fallbackMethod = "reserveStockFallback")
    @Retry(name = "product-service")
    public ProductReserveResponse reserveStock(Long productId, int qty) {
		log.info("Calling product-service reserve stock");

        String url = "http://product-service/products/"
                + productId + "/reserve";

        ProductReserveResponse response = restTemplate.postForObject(
                url,
                new ReserveStockRequest(qty),
                ProductReserveResponse.class
        );
        log.info("Attempt success reserveStock()");
        return response;
    }
	
	// Fallback method
    public ProductReserveResponse reserveStockFallback(Long productId, int qty, Throwable ex) {
    	log.error(
                "Reserve stock failed after retries. productId={}, qty={}",
                productId, qty, ex
            );
    	throw new ProductStockReserveException(productId);
    }
    
    /*@CircuitBreaker(name = "product-service", fallbackMethod = "restoreStockFallback")
    @Retry(name = "product-service")
    public void restoreStock(Long productId, int qty) {

        String url = "http://product-service/products/"
                + productId + "/restore";

        restTemplate.postForObject(
                url,
                new RestoreStockRequest(qty),
                Void.class
        );
    }
    
    // Fallback method
    public void restoreStockFallback(Long productId, int qty, Throwable ex) {
    	return new ProductReserveResponse(productId, 0.0);
    }*/
}

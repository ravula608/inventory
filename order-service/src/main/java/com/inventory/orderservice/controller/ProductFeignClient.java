package com.inventory.orderservice.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-service",
        path = "/products",
        fallback = ProductClientFallback.class
)
public interface ProductFeignClient {

    @PostMapping("/{productId}/restore")
    StockRestoreResult restoreStock(
            @PathVariable("productId") Long productId,
            @RequestBody RestoreStockRequest request
    );
}
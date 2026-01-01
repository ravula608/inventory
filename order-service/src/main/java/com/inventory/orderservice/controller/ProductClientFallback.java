package com.inventory.orderservice.controller;

import org.springframework.stereotype.Component;

@Component
public class ProductClientFallback implements ProductFeignClient {

    @Override
    public StockRestoreResult restoreStock(
            Long productId, RestoreStockRequest request) {

        return new StockRestoreResult(
                false,
                "Stock restore failed. Please retry later."
        );
    }
}

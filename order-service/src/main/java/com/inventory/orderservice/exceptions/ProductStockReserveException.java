package com.inventory.orderservice.exceptions;

public class ProductStockReserveException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProductStockReserveException(Long productId) {
		super("Product stock reserve failed for productId: " + productId);
	}

}

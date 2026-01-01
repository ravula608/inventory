package com.inventory.productservice.exceptions;

public class InsufficientStocksException extends RuntimeException {
	
	private static final long serialVersionUID = -596150943478827188L;

	public InsufficientStocksException(String productName) {
        super("Insufficient stock for product: " + productName);
    }

}

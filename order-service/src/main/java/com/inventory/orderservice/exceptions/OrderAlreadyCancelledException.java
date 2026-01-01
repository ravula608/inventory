package com.inventory.orderservice.exceptions;

public class OrderAlreadyCancelledException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrderAlreadyCancelledException(Long orderId) {
		super("Order already cancelled with orderId: " + orderId);
	}

}

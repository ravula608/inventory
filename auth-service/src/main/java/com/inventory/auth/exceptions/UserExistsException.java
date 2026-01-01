package com.inventory.auth.exceptions;

public class UserExistsException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserExistsException(String name) {
        super("User already there with this name: " + name);
    }
}

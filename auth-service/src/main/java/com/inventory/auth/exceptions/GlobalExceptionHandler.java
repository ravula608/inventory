package com.inventory.auth.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	@ExceptionHandler(UserExistsException.class)
	public ResponseEntity<ApiError> handleProductNotFound(UserExistsException ex, HttpServletRequest request) {

		ApiError error = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage(), request.getRequestURI(),
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	// fallback for all unhandled exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {

		ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error",
				request.getRequestURI(), LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return errors;
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuth(AuthenticationException ex, HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials", request.getRequestURI(),
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
}
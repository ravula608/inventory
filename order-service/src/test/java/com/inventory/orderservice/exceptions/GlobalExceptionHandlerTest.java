package com.inventory.orderservice.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleProductNotFound_returnsNotFoundApiError() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/products/1");

        ProductNotFoundException ex = new ProductNotFoundException(1L);

        ResponseEntity<ApiError> resp = handler.handleProductNotFound(ex, req);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        ApiError body = resp.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), body.status());
        assertTrue(body.message().contains("1"));
        assertEquals("/products/1", body.path());
    }

    @Test
    void handleInsufficientStock_returnsBadRequest() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/orders");

        InsufficientStocksException ex = new InsufficientStocksException("not enough");

        ResponseEntity<ApiError> resp = handler.handleInsufficientStock(ex, req);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        ApiError body = resp.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.status());
        assertEquals("Insufficient stock for product: not enough", body.message());
    }

    @Test
    void handleOrderAlreadyCancelled_returnsBadRequest() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/orders/1/cancel");

        OrderAlreadyCancelledException ex = new OrderAlreadyCancelledException(1L);

        ResponseEntity<ApiError> resp = handler.handleOrderAlreadyCancelled(ex, req);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().message().contains("1"));
    }

    @Test
    void handlestockReserveFail_returnsBadRequest() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/reserve");

        ProductStockReserveException ex = new ProductStockReserveException(2L);

        ResponseEntity<ApiError> resp = handler.handlestockReserveFail(ex, req);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().message().contains("2"));
    }

    @Test
    void handleOrderNotFound_returnsNotFound() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/orders/99");

        OrderNotFoundException ex = new OrderNotFoundException(99L);
        ResponseEntity<ApiError> resp = handler.handleOrderNotFound(ex, req);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertTrue(resp.getBody().message().contains("99"));
    }

    @Test
    void handleGenericException_returnsInternalServerError() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/any");

        Exception ex = new Exception("boom");
        ResponseEntity<ApiError> resp = handler.handleGenericException(ex, req);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        ApiError body = resp.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.status());
        assertEquals("Internal server error", body.message());
    }
}


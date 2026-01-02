package com.inventory.productservice.service;

import com.inventory.productservice.config.ProductClient;
import com.inventory.productservice.controller.Product;
import com.inventory.productservice.controller.ProductRepository;
import com.inventory.productservice.controller.ProductReserveResponse;
import com.inventory.productservice.controller.StockRestoreResult;
import com.inventory.productservice.exceptions.InsufficientStocksException;
import com.inventory.productservice.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductClient productClient;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sample;

    @BeforeEach
    void setUp() {
        sample = Product.builder()
                .productId(1L)
                .name("P1")
                .description("D")
                .price(10.0)
                .availableQuantity(5)
                .active(true)
                .build();
    }

    @Test
    void getAppointment_delegatesToClient() {
        when(productClient.getProducts()).thenReturn("ok");
        assertEquals("ok", productService.getAppointment());
    }

    @Test
    void createProduct_saves() {
        when(productRepository.save(sample)).thenReturn(sample);
        Product res = productService.createProduct(sample);
        assertEquals(sample, res);
    }

    @Test
    void updateProduct_updatesExisting() {
        when(productRepository.findById(sample.getProductId())).thenReturn(Optional.of(sample));
        when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class))).thenReturn(sample);

        Product updated = productService.updateProduct(sample, sample.getProductId());
        assertNotNull(updated);
    }

    @Test
    void getProduct_notFound_throws() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(99L));
    }

    @Test
    void getProducts_returnsList() {
        when(productRepository.findAll()).thenReturn(List.of(sample));
        assertEquals(1, productService.getProducts().size());
    }

    @Test
    void reserveStock_success() {
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sample));
        var resp = productService.reserveStock(1L, 2);
        assertEquals(1L, resp.productId());
    }

    @Test
    void reserveStock_insufficient_throws() {
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sample));
        assertThrows(InsufficientStocksException.class, () -> productService.reserveStock(1L, 10));
    }

    @Test
    void restoreStock_success() {
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sample));
        StockRestoreResult r = productService.restoreStock(1L, 3);
        assertTrue(r.success());
    }
}


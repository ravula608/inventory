package com.inventory.productservice.controller;

import com.inventory.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController controller;

    private Product sample;

    @BeforeEach
    void setUp() {
        sample = Product.builder().productId(1L).name("p").price(10.0).availableQuantity(5).build();
    }

    @Test
    void getAllProducts_delegates() {
        when(productService.getProducts()).thenReturn(List.of(sample));
        assertEquals(1, controller.getAllProducts().size());
    }

    @Test
    void getProducts1_delegates() {
        when(productService.getAppointment()).thenReturn("ok");
        assertEquals("ok", controller.getProducts1());
    }

    @Test
    void create_update_get_reserve_restore_delegates() {

        when(productService.createProduct(sample)).thenReturn(sample);
        when(productService.updateProduct(sample, 1L)).thenReturn(sample);
        when(productService.getProduct(1L)).thenReturn(sample);
        when(productService.reserveStock(1L, 2))
                .thenReturn(new ProductReserveResponse(1L, 10.0));
        when(productService.restoreStock(1L, 2))
                .thenReturn(new StockRestoreResult(true, "ok"));

        // create
        ResponseEntity<Product> createResponse = controller.createProduct(sample);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertEquals(sample, createResponse.getBody());

        // update
        assertEquals(sample, controller.updateProduct(sample, 1L));

        // get
        assertEquals(sample, controller.getProduct(1L));

        // reserve
        ProductReserveResponse reserve =
                controller.reserveStock(1L, new ReserveStockRequest(2));
        assertEquals(1L, reserve.productId());

        // restore
        StockRestoreResult restore =
                controller.restoreStock(1L, new RestoreStockRequest(2));
        assertEquals(true, restore.success());
    }

}


package com.inventory.productservice.controller;

import java.util.List;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.productservice.service.ProductService;

import jakarta.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Value("${message}")
	String message;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public List<Product> getAllProducts() {
		return productService.getProducts();
	}
	
	@GetMapping("/get")
	public String getProducts1() {
		return productService.getAppointment();
	}
	
	@PostMapping("/admin")
	public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
	}
	
	@PutMapping("/{id}")
	public Product updateProduct(@RequestBody Product product, @PathVariable Long id) {
		return productService.updateProduct(product, id);
	}
	
	@GetMapping("/{id}")
    @Observed(name = "getProduct-service", contextualName = "getProduct-controller")
	public Product getProduct(@PathVariable Long id) {
        log.info("Fetching product with id: {}", id);
	    return productService.getProduct(id);
	}
	
	@PostMapping("/{productId}/reserve")
	public ProductReserveResponse reserveStock(@PathVariable("productId") Long productId, @RequestBody ReserveStockRequest request) {
		log.info("Reserving stock for productId: {}, quantity: {}", productId, request.quantity());
		return productService.reserveStock(productId, request.quantity());
	}
	
	@PostMapping("/{productId}/restore")
    @Observed(name = "restoreStock-service", contextualName = "restoreStock-controller")
    public StockRestoreResult restoreStock(
            @PathVariable("productId") Long productId,
            @RequestBody RestoreStockRequest request) {
        log.info("cancel stock called: productid={}", productId);

        return productService.restoreStock(productId, request.quantity());
    }


}

package com.inventory.productservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.productservice.config.ProductClient;
import com.inventory.productservice.controller.Product;
import com.inventory.productservice.controller.ProductRepository;
import com.inventory.productservice.controller.ProductReserveResponse;
import com.inventory.productservice.controller.StockRestoreResult;
import com.inventory.productservice.exceptions.InsufficientStocksException;
import com.inventory.productservice.exceptions.ProductNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductClient productClient;
	
	@Autowired
	private ProductRepository productRepository;
	
	public String getAppointment() {
		return productClient.getProducts();
	}

	public Product createProduct(Product product) {
		return productRepository.save(product);
		
	}

	public Product updateProduct(Product product, Long id) {
		Product products = productRepository.findById(product.getProductId())
				.orElseThrow(() -> new ProductNotFoundException(id));
		products = Product.builder()
				.productId(id)
				.name(Optional.ofNullable(product.getName()).orElse(products.getName()))
				.description(Optional.ofNullable(product.getDescription()).orElse(products.getDescription()))
				//.availableQuantity(Optional.ofNullable(product.getAvailableQuantity()).orElse(products.getAvailableQuantity())) // can not update stocks
				.price(Optional.ofNullable(product.getPrice()).orElse(products.getPrice()))
				.createdAt(products.getCreatedAt())
				.updatedAt(LocalDateTime.now())
		.build();
		return productRepository.save(products);
	}

	public Product getProduct(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}

	public List<Product> getProducts() {
		return productRepository.findAll();
	}
	
	@Transactional
	public ProductReserveResponse reserveStock(Long productId, int qty) {

	    Product product = productRepository.findByIdForUpdate(productId)
	            .orElseThrow(() -> new ProductNotFoundException(productId));

	    if (product.getAvailableQuantity() < qty) {
	        throw new InsufficientStocksException(product.getName());
	    }

		product.setAvailableQuantity(product.getAvailableQuantity() - qty);

		return new ProductReserveResponse(product.getProductId(), product.getPrice());
	}
	
	@Transactional
    public StockRestoreResult restoreStock(Long productId, int qty) {

        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(productId));

        product.setAvailableQuantity(
                product.getAvailableQuantity() + qty
        );
        return new StockRestoreResult(true, "Stock restored successfully");
    }

	
}

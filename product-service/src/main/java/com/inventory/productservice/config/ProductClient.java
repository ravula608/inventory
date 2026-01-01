package com.inventory.productservice.config;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", path = "/orders")
public interface ProductClient {

    /*@GetMapping
    List<Product> getAllProducts();

    @GetMapping("/{id}")
    Product getProductById(@PathVariable("id") Long id);*/
	
	@GetMapping("/get")
	String getProducts();
}

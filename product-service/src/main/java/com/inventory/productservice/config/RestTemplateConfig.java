package com.inventory.productservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced   // REQUIRED if you call other services by name (auth-service, order-service)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

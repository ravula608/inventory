package com.inventory.productservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Temporarily disabled")
public class ProductServiceApplicationTest {

    @Test
    void main_runsWithoutException() {
        System.setProperty("eureka.client.enabled", "false");
        System.setProperty("spring.cloud.discovery.enabled", "false");
        ProductServiceApplication.main(new String[]{});
    }
}


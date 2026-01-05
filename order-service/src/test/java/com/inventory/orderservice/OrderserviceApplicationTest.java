package com.inventory.orderservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Temporarily disabled")
public class OrderserviceApplicationTest {

    @Test
    void main_runsWithoutException() {
        // Prevent Eureka client from trying to contact a server during test
        System.setProperty("eureka.client.enabled", "false");
        System.setProperty("spring.cloud.discovery.enabled", "false");

        OrderserviceApplication.main(new String[]{});
    }
}

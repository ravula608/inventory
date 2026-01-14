package com.inventory.productservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//@Disabled("Temporarily disabled")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class ProductServiceApplicationTest {

    @Test
    void contextLoads() {
        // If context starts, test passes
    }
}


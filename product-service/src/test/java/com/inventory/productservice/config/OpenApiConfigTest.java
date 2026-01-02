package com.inventory.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenApiConfigTest {

    @Test
    void beans_create_expected_objects() {
        OpenApiConfig cfg = new OpenApiConfig();
        OpenAPI api = cfg.securedOpenAPI();
        assertNotNull(api);
        assertTrue(api.getComponents().getSecuritySchemes().containsKey("bearerAuth"));

        Operation op = new Operation();
        Operation res = cfg.globalHeaderCustomizer().customize(op, null);
        assertNotNull(res);
        assertTrue(res.getParameters().stream().anyMatch(p -> "X-Internal-Gateway".equals(p.getName())));
    }
}


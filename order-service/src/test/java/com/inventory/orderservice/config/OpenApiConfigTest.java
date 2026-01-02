package com.inventory.orderservice.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import org.junit.jupiter.api.Test;

import org.springframework.web.method.HandlerMethod;

public class OpenApiConfigTest {

    @Test
    void securedOpenAPI_and_globalHeaderCustomizer_work() {
        OpenApiConfig cfg = new OpenApiConfig();

        OpenAPI openAPI = cfg.securedOpenAPI();
        assertNotNull(openAPI);
        // ensure security scheme exists
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("bearerAuth"));

        Operation op = new Operation();
        Operation result = cfg.globalHeaderCustomizer().customize(op, (HandlerMethod) null);
        assertNotNull(result);
        assertTrue(result.getParameters().stream().anyMatch(p -> "X-Internal-Gateway".equals(p.getName())));
    }
}


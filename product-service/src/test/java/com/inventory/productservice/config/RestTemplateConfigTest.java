package com.inventory.productservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RestTemplateConfigTest {

    @Test
    void restTemplate_bean_provides_instance() {
        RestTemplateConfig cfg = new RestTemplateConfig();
        RestTemplate rt = cfg.restTemplate();
        assertNotNull(rt);
    }
}


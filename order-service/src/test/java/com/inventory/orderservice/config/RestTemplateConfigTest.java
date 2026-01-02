package com.inventory.orderservice.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

public class RestTemplateConfigTest {

    @Test
    void restTemplate_containsInterceptor() {
        RestTHeaderForwardingInterceptor interceptor = new RestTHeaderForwardingInterceptor();
        RestTemplateConfig cfg = new RestTemplateConfig();

        RestTemplate rt = cfg.restTemplate(interceptor);
        assertNotNull(rt);
        boolean found = rt.getInterceptors().stream()
                .anyMatch(i -> i.getClass().equals(RestTHeaderForwardingInterceptor.class));
        assertNotNull(found);
    }
}


package com.inventory.productservice.config;

import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeignClientHeaderForwardInterceptorTest {

    @Test
    void apply_forwardsHeaders_whenAttrsPresent() {
        FeignClientHeaderForwardInterceptor interceptor = new FeignClientHeaderForwardInterceptor();

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("X-Internal-Gateway")).thenReturn("inventory-gateway");
        Mockito.when(req.getHeader("X-User-Id")).thenReturn("5");
        Mockito.when(req.getHeader("X-User-Role")).thenReturn(null);

        ServletRequestAttributes attrs = Mockito.mock(ServletRequestAttributes.class);
        Mockito.when(attrs.getRequest()).thenReturn(req);

        try (MockedStatic<RequestContextHolder> r = Mockito.mockStatic(RequestContextHolder.class)) {
            r.when(RequestContextHolder::getRequestAttributes).thenReturn(attrs);
            RequestTemplate template = new RequestTemplate();
            interceptor.apply(template);

            assertTrue(template.headers().containsKey("X-Internal-Gateway"));
            assertTrue(template.headers().containsKey("X-User-Id"));
        }
    }

    @Test
    void apply_noop_when_no_attrs() {
        FeignClientHeaderForwardInterceptor interceptor = new FeignClientHeaderForwardInterceptor();
        try (MockedStatic<RequestContextHolder> r = Mockito.mockStatic(RequestContextHolder.class)) {
            r.when(RequestContextHolder::getRequestAttributes).thenReturn(null);
            RequestTemplate template = new RequestTemplate();
            interceptor.apply(template);
            assertTrue(template.headers().isEmpty());
        }
    }
}


package com.inventory.orderservice.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class FeignClientHeaderForwardInterceptorTest {

    @Test
    void apply_forwardsHeaders_whenRequestAttributesPresent() {
        FeignClientHeaderForwardInterceptor interceptor = new FeignClientHeaderForwardInterceptor();

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("X-Internal-Gateway")).thenReturn("inventory-gateway");
        Mockito.when(req.getHeader("X-User-Id")).thenReturn("5");
        Mockito.when(req.getHeader("X-User-Role")).thenReturn(null);

        ServletRequestAttributes attrs = Mockito.mock(ServletRequestAttributes.class);
        Mockito.when(attrs.getRequest()).thenReturn(req);

        try (MockedStatic<RequestContextHolder> rc = Mockito.mockStatic(RequestContextHolder.class)) {
            rc.when(RequestContextHolder::getRequestAttributes).thenReturn(attrs);

            RequestTemplate template = new RequestTemplate();
            interceptor.apply(template);

            // headers forwarded
            assertTrue(template.headers().containsKey("X-Internal-Gateway"));
            assertTrue(template.headers().containsKey("X-User-Id"));
            assertTrue(!template.headers().containsKey("X-User-Role"));
        }
    }

    @Test
    void apply_noop_whenNoRequestAttributes() {
        FeignClientHeaderForwardInterceptor interceptor = new FeignClientHeaderForwardInterceptor();
        try (MockedStatic<RequestContextHolder> rc = Mockito.mockStatic(RequestContextHolder.class)) {
            rc.when(RequestContextHolder::getRequestAttributes).thenReturn(null);
            RequestTemplate template = new RequestTemplate();
            interceptor.apply(template);
            assertTrue(template.headers().isEmpty());
        }
    }
}


package com.inventory.orderservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GatewayOnlyFilterTest {

    private GatewayOnlyFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new GatewayOnlyFilter();
    }

    @Test
    void shouldAllowOpenApiPathWithoutHeader() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/v3/api-docs/test");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void shouldForbidDirectAccessWhenHeaderMissing() throws Exception {
        when(request.getRequestURI()).thenReturn("/products/1/reserve");
        when(request.getHeader("X-Internal-Gateway")).thenReturn(null);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        filter.doFilterInternal(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        pw.flush();
        String body = sw.toString();
        assertTrue(body.contains("Direct access forbidden"));
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void shouldAllowWhenHeaderPresent() throws Exception {
        when(request.getRequestURI()).thenReturn("/products/1/reserve");
        when(request.getHeader("X-Internal-Gateway")).thenReturn("inventory-gateway");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}


package com.inventory.productservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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
    void allow_open_api_paths() throws Exception {
        when(request.getRequestURI()).thenReturn("/v3/api-docs/test");
        filter.doFilterInternal(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void forbid_when_header_missing() throws Exception {
        when(request.getRequestURI()).thenReturn("/products/1/reserve");
        when(request.getHeader("X-Internal-Gateway")).thenReturn(null);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        filter.doFilterInternal(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        pw.flush();
        assertTrue(sw.toString().contains("Direct access forbidden"));
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void allow_when_header_present() throws Exception {
        when(request.getRequestURI()).thenReturn("/products/1/reserve");
        when(request.getHeader("X-Internal-Gateway")).thenReturn("inventory-gateway");

        filter.doFilterInternal(request, response, chain);
        verify(chain).doFilter(request, response);
    }
}


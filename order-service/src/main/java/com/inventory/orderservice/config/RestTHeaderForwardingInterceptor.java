package com.inventory.orderservice.config;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class RestTHeaderForwardingInterceptor implements ClientHttpRequestInterceptor {

    private static final List<String> HEADERS =
        List.of("X-Internal-Gateway", "X-User-Id", "X-User-Role");

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        ServletRequestAttributes attrs =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest incoming = attrs.getRequest();

            HEADERS.forEach(header -> {
                String value = incoming.getHeader(header);
                if (value != null) {
                    request.getHeaders().add(header, value);
                }
            });
        }

        return execution.execute(request, body);
    }
}


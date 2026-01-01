
package com.inventory.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter {
	
    @Autowired
    private JwtUtil jwtUtil;
    
    private static final String INTERNAL_HEADER = "X-Internal-Gateway";
    private static final String INTERNAL_VALUE = "inventory-gateway";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        System.out.println("JWT FILTER TRIGGERED");

        String path = exchange.getRequest().getURI().getPath();

        // Allow auth service
        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims;
        try {
            claims = jwtUtil.validateToken(authHeader.substring(7));
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String role = claims.get("role", String.class);

        // 🔐 ROLE RULES
        if (path.startsWith("/admin") && !"ADMIN".equals(role)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        if ((path.startsWith("/products") || path.startsWith("/orders")) && !("USER".equals(role) || "ADMIN".equals(role))) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        
     // 🔐 Forward trusted internal header + user info
        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(INTERNAL_HEADER, INTERNAL_VALUE)
                .header("X-User-Id", claims.get("userId").toString())
                .header("X-User-Role", claims.get("role").toString())
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
}

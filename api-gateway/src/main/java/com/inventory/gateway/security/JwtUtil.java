package com.inventory.gateway.security;

import java.security.Key;
import java.util.Base64;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET =
	        "ZHVtbXktMzItYnl0ZS1iYXNlNjQtc2VjcmV0LWtleS1mb3Itand0LXNlcnZpY2Vz";

	    private final Key key =
	        Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

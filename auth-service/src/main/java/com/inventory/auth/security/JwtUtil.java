
package com.inventory.auth.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET =
	        "ZHVtbXktMzItYnl0ZS1iYXNlNjQtc2VjcmV0LWtleS1mb3Itand0LXNlcnZpY2Vz";

	    private final Key key =
	        Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

    public String generateToken(String username, String role, Long userId) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role).claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 3600_000)
                )
                .signWith(key)
                .compact();
    }
}




package com.inventory.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.auth.security.CustomUserDetails;
import com.inventory.auth.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<String> login(@Validated @RequestBody LoginRequest req) {
		log.info("Login API: authentication");

		try {
			Authentication auth = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));

			CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
			String role = user.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
			Long userId = user.getUserId();

			String token = jwtUtil.generateToken(user.getUsername(), role, userId);
			return ResponseEntity.ok(token);
		} catch (AuthenticationException ex) {
			log.warn("Authentication failed for user {}: {}", req.username(), ex.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}
}

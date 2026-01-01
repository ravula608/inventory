package com.inventory.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.auth.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class TestController {
	
	@Autowired
    JwtUtil jwtUtil;
	
	@Value("${message}")
	String message;
	
	@GetMapping("/test")
	public String get() {
		return message;
	}

}

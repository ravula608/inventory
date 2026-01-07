package com.inventory.auth.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.auth.exceptions.UserExistsException;
import com.inventory.auth.security.User;
import com.inventory.auth.security.UserRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class RegisterController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
		log.info("Register API starting");

		if (userRepository.findByUsername(request.username()).isPresent()) {
			throw new UserExistsException(request.username());
		}

		User user = new User();
		user.setUsername(request.username());

		// HASH PASSWORD BEFORE SAVE
		user.setPassword(passwordEncoder.encode(request.password()));

		user.setRole(Optional.ofNullable(request.role()).orElse("USER"));

		userRepository.save(user);
		log.info("User registered successfully for {}", request.username());
        RegisterResponse registerResponse = new RegisterResponse(user.getUserId(), user.getUsername(), user.getRole(),
				"User registered successfully");
        return  ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
	}
}

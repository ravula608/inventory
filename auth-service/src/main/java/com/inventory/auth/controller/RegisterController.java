package com.inventory.auth.controller;

import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.auth.exceptions.UserExistsException;
import com.inventory.auth.security.User;
import com.inventory.auth.security.UserRepository;

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
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
    	log.info("Register API starting");
    	
        if ( userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserExistsException(request.username());
        }

        User user = new User();
        user.setUsername(request.username());

        // HASH PASSWORD BEFORE SAVE
        user.setPassword(passwordEncoder.encode(request.password()));

        user.setRole(Optional.ofNullable(request.role()).orElse("USER"));

        userRepository.save(user);
        log.info("User registered successfully for {}", request.username());
        return new RegisterResponse(user.getUserId(), user.getUsername(), user.getRole(), "User registered successfully");
    }
}

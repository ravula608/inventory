package com.inventory.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inventory.auth.security.User;
import com.inventory.auth.security.UserRepository;

//@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository repo;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) {

        /*if (!repo.findByUsername("Ram")) {
            User user = new User();
            user.setUsername("Ram");
            user.setPassword(encoder.encode("Ramanji"));
            user.setRole("ADMIN");
            repo.save(user);
        }*/
    }
}

package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUsername("sue");
        user.setFullname("Sue BDH");
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode("123"));
    }

    // ============================================================
    // CREATE
    // ============================================================
    @Test
    void shouldCreateUser() {
        User saved = userRepository.save(user);

        assertNotNull(saved.getUserId());
        assertEquals("sue", saved.getUsername());
        assertTrue(passwordEncoder.matches("123", saved.getPassword()));
    }

    // ============================================================
    // READ
    // ============================================================
    @Test
    void shouldFindUserById() {
        User saved = userRepository.save(user);

        Optional<User> result = userRepository.findById(saved.getUserId());
        assertTrue(result.isPresent());

        User found = result.get();
        assertEquals("sue", found.getUsername());
        assertEquals("Sue BDH", found.getFullname());
        assertTrue(passwordEncoder.matches("123", found.getPassword()));
    }

    // ============================================================
    // UPDATE
    // ============================================================
    @Test
    void shouldUpdateUser() {
        User saved = userRepository.save(user);

        saved.setFullname("Sousou Updated");
        saved.setPassword(passwordEncoder.encode("456"));
        saved.setRole("ADMIN");

        User updated = userRepository.save(saved);

        assertEquals("Sousou Updated", updated.getFullname());
        assertEquals("ADMIN", updated.getRole());
        assertTrue(passwordEncoder.matches("456", updated.getPassword()));
    }

    // ============================================================
    // DELETE
    // ============================================================
    @Test
    void shouldDeleteUser() {
        User saved = userRepository.save(user);

        userRepository.deleteById(saved.getUserId());

        Optional<User> result = userRepository.findById(saved.getUserId());
        assertFalse(result.isPresent());
    }
}

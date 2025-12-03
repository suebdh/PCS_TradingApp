package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    // Helper pour créer un user propre
    private User buildUser(String username, String fullname, String role, String rawPassword) {
        User u = new User();
        u.setUsername(username);
        u.setFullname(fullname);
        u.setRole(role);
        u.setPassword(passwordEncoder.encode(rawPassword));
        return u;
    }

    // ============================================================
    // CREATE
    // ============================================================
    @Test
    void shouldCreateUser() {
        User user = buildUser("sue_create", "Sue BDH", "USER", "123");

        User saved = userRepository.save(user);

        assertNotNull(saved.getUserId());
        assertEquals("sue_create", saved.getUsername());
        assertTrue(passwordEncoder.matches("123", saved.getPassword()));
    }

    // ============================================================
    // READ
    // ============================================================
    @Test
    void shouldFindUserById() {
        User saved = userRepository.save(buildUser("sue_read", "Sue BDH", "USER", "123"));

        Optional<User> result = userRepository.findById(saved.getUserId());
        assertTrue(result.isPresent());

        User found = result.get();
        assertEquals("sue_read", found.getUsername());
        assertEquals("Sue BDH", found.getFullname());
        assertTrue(passwordEncoder.matches("123", found.getPassword()));
    }

    // ============================================================
    // UPDATE
    // ============================================================
    @Test
    void shouldUpdateUser() {
        User saved = userRepository.save(buildUser("sue_update", "Sue BDH", "USER", "123"));

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
        User saved = userRepository.save(buildUser("sue_delete", "Sue BDH", "USER", "123"));

        userRepository.deleteById(saved.getUserId());

        Optional<User> result = userRepository.findById(saved.getUserId());
        assertFalse(result.isPresent());
    }
}

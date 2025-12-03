package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    // Méthode dérivée Spring Data JPA : équivalent de "SELECT * FROM users WHERE username = ?" car username est un champ dans mon entité "User".
    // Spring génère automatiquement la requête à partir du nom de méthode.
    Optional<User> findByUsername(String username);
}

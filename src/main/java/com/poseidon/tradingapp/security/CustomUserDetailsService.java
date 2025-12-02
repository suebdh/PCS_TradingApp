package com.poseidon.tradingapp.security;

import com.poseidon.tradingapp.domain.User;
import com.poseidon.tradingapp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("Spring Security cherche l'utilisateur : " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur non trouvé : " + username));


        System.out.println("Utilisateur trouvé : " + user.getUsername() + ", rôle = " + user.getRole());

        // Conversion de notre User JPA vers un UserDetails Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())       // déjà hashé (BCrypt)
                .roles(user.getRole())              // ex: "ADMIN" ou "USER"
                .build();
    }
}

package com.poseidon.tradingapp.security;

import com.poseidon.tradingapp.domain.User;
import com.poseidon.tradingapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implémentation personnalisée de {@link UserDetailsService} utilisée par Spring Security pour
 * charger un utilisateur depuis la base MySQL lors de l'authentification
 *
 * <p>
 * Cette classe convertit un objet {@link User} (entité JPA)
 * en {@link org.springframework.security.core.userdetails.User} (objet de Spring Security)
 * </p>
 *
 * <p>
 *     Les rôles stockés en base (ex : "ADMIN") sont automatiquement mappés vers les authorities Spring Security (ex : "ROLE_ADMIN")
 *     via la méthode {@code roles()}
 * </p>
 */
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur via son username
     * Méthode appelée automatiquement par Spring Security lors du login
     * <p>
     *     Étapes :
     *     <ul>
     *         <li>Recherche de l'utilisateur en base via UserRepository</li>
     *         <li>Lance une {@link com.poseidon.tradingapp.exceptions.UserNotFoundException}, si absent</li>
     *         <li>Conversion vers un objet UserDetails compris par Spring Security</li>
     *         <li>Ajout automatique du préfixe "ROLE_" grâce à {@code roles(user.getRole())}</li>
     *     </ul>
     *
     * </p>
     * @param username nom d'utilisateur fournir lors du login
     * @return un UserDetails requis par Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur n'existe avec ce username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Spring Security cherche l'utilisateur : {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur non trouvé : " + username));


        log.debug("Utilisateur trouvé : {}, rôle = {}", user.getUsername(), user.getRole());

        // Conversion de notre User JPA vers un UserDetails Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())       // déjà hashé (BCrypt)
                .roles(user.getRole())              // ex: "ADMIN" ou "USER"
                .build();
    }
}

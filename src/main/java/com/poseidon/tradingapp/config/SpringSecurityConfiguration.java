package com.poseidon.tradingapp.config;

import com.poseidon.tradingapp.repositories.UserRepository;
import com.poseidon.tradingapp.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration centrale de Spring Security pour l'ensemble de l'application PCS_TradingApp
 * <p>Cette classe :
 *     <ul>
 *       <li>Définit les règles d'accès aux différentes URL</li>
 *      <li>Configure la page de Login personnalisée</li>
 *      <li>Active l'authentification basée sur les utilisateurs stockés en base MySQL</li>
 *      <li>Enregistre le filtre de sécurité principal (SecurityFilterChain)</li>
 *      <li>Déclare le PasswordEncoder (BCrypt)</li>
 *     </ul>
 * </p>
 * <p>
 *     Règles d'accès :
 *     <ul>
 *         <li>Accès public : Ressources statiques, "/", page de login</li>
 *         <li>Accès réservé ADMIN : /user/**</li>
 *         <li>Accès authentifié (USER ou ADMIN) : bidlist, trade, rating, curvepoint et rule</li>
 *     </ul>
 * </p>
 * <p>
 *     Remarque : La protection CSRF est désactivée temporairement pour les tests et la simplification du dév.
 *     Elle pourra être réactivée si nécessaire.
 * </p>
 */
@Configuration
public class SpringSecurityConfiguration {
    /**
     * Cette méthode configure :
     * <ul>
     * <li>Les règles d'autorisation (public, authentifié, ADMIN uniquement)</li>
     * <li>La page de login personnalisée</li>
     * <li>La gestion de l'erreur 403</li>
     * <li>Le mécanisme de Logout</li>
     * <li>La désactivation temporaire du CSRF</li>
     * </ul>
     *
     * @param http objet HttpSecurity fourni par Spring Security
     * @return SecurityFilterChain retourné
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // les ressources statiques restent publiques + la racine du projet
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/app/login").permitAll()

                        // accès ADMIN uniquement pour la gestion des utilisateurs
                        .requestMatchers("/user/**").hasRole("ADMIN")

                        // accès à tous les utilisateurs authentifiés (USER ou ADMIN)
                        .requestMatchers("/bidList/**", "/curvePoint/**", "/trade/**", "/rating/**", "/rule/**").authenticated()
                        // tout le reste nécessitera une authentification
                        .anyRequest().authenticated()
                )
                // Désactivation TEMPORAIRE de la protection CSRF (Cross-Site Request Forgery)
                // ---------------------------------------------------------------
                // Par défaut, Spring Security exige un jeton CSRF pour toutes les requêtes POST/PUT/DELETE afin d'empêcher les attaques de type "requête intersite" (CSRF).
                // En phase de développement, on la désactive pour simplifier les tests des formulaires, car on n'a pas encore mis en place l'authentification complète ni l'injection du jeton CSRF dans les pages Thymeleaf.
                // ATTENTION : À réactiver dès que les formulaires utilisateurs (login, création, etc.) seront finalisés.
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginPage("/app/login")             // URL GET pour afficher le formulaire de login
                        .loginProcessingUrl("/app/login")    // URL POST TRAITÉE par Spring Security
                        .defaultSuccessUrl("/bidList/list", true)  // redirection après succès
                        .failureUrl("/app/login?error=true") // gestion des erreurs
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/app/error")
                )
                .logout(logout -> logout
                        .logoutUrl("/app-logout")            // action logout
                        .logoutSuccessUrl("/")               // après déconnexion → home
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Fournit une implémentation personnalisée de {@link UserDetailsService} permettant à Spring Security de
     * charger les utilisateurs depuis la base MySQL (username, mot de passe BCrypt, rôle)
     * @param userRepository repository d'accès à la table users
     * @return une instance de {@link CustomUserDetailsService}
     */
    // Active l'authentification Spring Security via les utilisateurs stockés en base MySQL (username, mot de passe BCrypt, rôle)
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    /**
     * Déclare le PasswordEncoder utilisé pour hasher les mots de passe
     * BCrypt est l'algorithme recommandé par Spring Security
     * @return PasswordEncoder basé sur BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

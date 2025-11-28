package com.poseidon.tradingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

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
                        .loginPage("/app/login")             // page de login personnalisée
                        .defaultSuccessUrl("/bidList/list", true)  // redirection après succès
                        .failureUrl("/app/login?error=true") // gestion des erreurs
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/app-logout")            // action logout
                        .logoutSuccessUrl("/")               // après déconnexion → home
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

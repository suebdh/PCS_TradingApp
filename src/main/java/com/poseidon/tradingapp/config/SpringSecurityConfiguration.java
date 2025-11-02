package com.poseidon.tradingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // on autorise explicitement TOUTES les routes de RuleController
                        .requestMatchers("/rule/**", "/trade/**").permitAll()
                        // on autorise les ressources statiques
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // on autorise la racine également
                        .requestMatchers("/").permitAll()
                        // tout le reste nécessitera une authentification
                        .anyRequest().authenticated()
                )
                // Désactivation TEMPORAIRE de la protection CSRF (Cross-Site Request Forgery)
                // ---------------------------------------------------------------
                // Par défaut, Spring Security exige un jeton CSRF pour toutes les requêtes POST/PUT/DELETE afin d'empêcher les attaques de type "requête intersite" (CSRF).
                // En phase de développement, on la désactive pour simplifier les tests des formulaires, car on n'a pas encore mis en place l'authentification complète ni l'injection du jeton CSRF dans les pages Thymeleaf.
                // ATTENTION : À réactiver dès que les formulaires utilisateurs (login, création, etc.) seront finalisés.
                .csrf(AbstractHttpConfigurer::disable)
                // on active le formulaire de login par défaut
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.poseidon.tradingapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration d'environnement pour l'application Poseidon Capital Solutions.
 * <p>
 * Cette classe détecte automatiquement le profil Spring actif
 * ({@code dev}, {@code test} ou {@code prod})
 * et charge le fichier <b>.env</b> correspondant :
 * <ul>
 *     <li>{@code .env.dev} — utilisé par défaut (profil de développement)</li>
 *     <li>{@code .env.test} — utilisé pour les tests</li>
 *     <li>{@code .env.prod} — utilisé en production</li>
 * </ul>
 * Si aucun profil n'est défini, le profil {@code dev} est appliqué par défaut.
 * </p>
 *
 * <p>
 * Les variables d'environnement suivantes sont attendues :
 * <ul>
 *     <li>{@code DB_USERNAME}</li>
 *     <li>{@code DB_PASSWORD}</li>
 * </ul>
 * Elles sont injectées dans les propriétés système afin d'être accessibles
 * par la configuration Spring Boot (ex. {@code application.properties}).
 * </p>
 *
 * <p>
 * Si le fichier correspondant n'est pas trouvé,
 * la configuration effectue un fallback sur un éventuel fichier générique {@code .env}.
 * </p>
 *
 * @author Sarar
 */

@Configuration
public class EnvConfig {

    static {
        // Chercher le profil actif soit via VM option ou bien via variable d'environnement système
        String profile = System.getProperty("spring.profiles.active",
                System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "dev"));

        // Choisir le fichier .env correspondant
        String envFile = switch (profile) {
            case "test" -> ".env.test";
            case "prod" -> ".env.prod";
            default -> ".env.dev"; // dev par défaut
        };

        // Charger le .env du profil, avec fallback sur le .env classique si absent
        Dotenv dotenv = Dotenv.configure()
                .filename(envFile)
                .ignoreIfMissing()
                .load();

        if (dotenv.get("DB_USERNAME") == null && dotenv.get("DB_PASSWORD") == null) {
            // fallback éventuel sur .env si utilisé un en local
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        }

        // Injecter les credentials
        String user = dotenv.get("DB_USERNAME");
        String pass = dotenv.get("DB_PASSWORD");

        if (user != null) System.setProperty("DB_USERNAME", user);
        if (pass != null) System.setProperty("DB_PASSWORD", pass);

        System.out.println(" EnvConfig > profil actif: " + profile + " | fichier: " + envFile);
    }
}

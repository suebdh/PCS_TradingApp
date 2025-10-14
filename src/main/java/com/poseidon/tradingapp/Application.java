package com.poseidon.tradingapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * Point d'entrée principal de l'application Poseidon Capital Solutions.
 * <p>
 * Cette classe initialise le contexte Spring Boot (création des Beans, chargement des configurations)
 * et démarre le serveur web embarqué (Tomcat par défaut).
 * </p>
 *
 * <p><b>Fonctionnalités :</b></p>
 * <ul>
 *     <li>Affiche le profil actif au démarrage ({@code dev}, {@code test}, ou {@code prod}).</li>
 *     <li>Affiche l'URL de connexion JDBC actuellement utilisée.</li>
 * </ul>
 *
 * <p><b>Annotation :</b></p>
 * <ul>
 *     <li>{@code @SpringBootApplication} — active la configuration automatique, le scan des composants et les fonctionnalités Spring Boot.</li>
 * </ul>
 *
 * @author Sarar
 */
@Slf4j
@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		log.info("Démarrage de l'application PCS...");
		var ctx = SpringApplication.run(Application.class, args);
		Environment env = ctx.getEnvironment();
		String[] profiles = env.getActiveProfiles();
		String profile = profiles.length > 0 ? profiles[0] : "default";
		log.info("Profil actif : {}", profile);
		log.info("JDBC URL : {}", env.getProperty("spring.datasource.url"));
		log.info("Application PCS démarrée avec succès !");
	}
}

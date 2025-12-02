# Poseidon Capital Solutions - Trading App
> Application Spring Boot permettant **la gestion des offres, courbes, notations, règles et transactions financières** pour Poseidon Capital Solutions.  
> Projet 7 du parcours **Développeur Java - OpenClassrooms**.

---

## Stack technique

| Technologie              | Version | Usage                                                     |
|--------------------------|---------|-----------------------------------------------------------|
| **Spring Boot**          | 3.5.6   | Framework principal (Web, Data JPA, Security, Validation) |
| **Java**                 | 17      | Version LTS utilisée pour compatibilité Spring Boot 3     |
| **Thymeleaf**            | 3.x     | Moteur de templates HTML côté serveur                     |
| **Bootstrap**            | 4.3.1   | Mise en forme du front-end                                |
| **Hibernate / JPA**      | Intégré | ORM pour la gestion des entités et du mapping SQL         |
| **MySQL**                | 8.x     | Base de données principale                                |
| **H2**                   | 2.x     | Base en mémoire pour les tests                            |
| **Lombok**               | 1.18.30 | Génération automatique des getters/setters                |
| **dotenv (java-dotenv)** | 5.2.2   | Gestion sécurisée des variables d'environnement (.env)    |
| **JUnit 5 (Jupiter)**    | 5.x     | Framework de tests unitaires                              |
| **Spring Security**      | 6.x     | Authentification *session-based* (non-JWT)                |

## Fonctionnalités implémentées

- Gestion des **règles métier (Rule)** : création, mise à jour, suppression, liste, validation des doublons
- Gestion des messages utilisateur centralisée via `MessageUtils`
- Logs applicatifs ajoutés pour tracer les actions clés (ajout, édition, suppression)
- Architecture conforme au modèle MVC (Controller ↔ Service ↔ Repository)
- En cours : mise en place des tests unitaires et d'intégration (JUnit 5)

## Setup with Intellij IDE
1. Create project from Initializr: File > New > project > Spring Initializr
2. Add lib repository into pom.xml
3. Add folders
    - Source root: src/main/java
    - View: src/main/resources
    - Static: src/main/resource/static
4. Create database with name "demo" as configuration in application.properties
5. Run sql script to create table doc/data.sql

## Initialisation de la base MySQL (profil DEV)

Lors du tout **premier démarrage** de l'application en environnement **DEV**, Spring doit exécuter automatiquement les scripts SQL situés dans :

- `src/main/resources/database/schema.sql`
- `src/main/resources/database/data.sql`

Pour que cette initialisation soit bien effectuée, la configuration suivante doit être activée dans `application-dev.properties` :

spring.sql.init.mode=always

Cela permet :

- de créer automatiquement la table `users`
- d'insérer les comptes initiaux `admin/admin` et `user/user`
- d'appliquer la contrainte d'unicité sur `username`

### Après le premier démarrage

Une fois la base initialisée, il est recommandé de repasser la propriété à :

spring.sql.init.mode=never

Ainsi, on évite que :

- les données soient réinsérées à chaque lancement.
- des erreurs telles que `Duplicate entry 'admin' for key 'username_UNIQUE'` apparaissent

Résumé

| Étape               | Valeur recommandée                      |
|---------------------|-----------------------------------------|
| Premier lancement   | spring.sql.init.mode=always             |
| Lancements suivants | spring.sql.init.mode=never              |


## Structure du projet et bonnes pratiques

1. **Domain** → Contient les entités JPA (ex : `Rule`)
2. **Repositories** → Interfaces Spring Data JPA
3. **Services** → Logique métier, validation, conversions DTO ↔ entité
4. **Controllers** → Gestion des routes web et messages flash
5. **Utils** → Classes utilitaires (ex : `MessageUtils`)
6. **Config** → Configuration Spring (sécurité, environnement, etc.)

## Security
1. Create user service to load user from  database and place in package com.poseidon.tradingapp.services
2. Add configuration class and place in package com.poseidon.tradingapp.config

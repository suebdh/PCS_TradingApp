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
| **dotenv (java-dotenv)** | 5.2.2   | Gestion sécurisée des variables d’environnement (.env)    |
| **JUnit 5 (Jupiter)**    | 5.x     | Framework de tests unitaires                              |
| **Spring Security**      | 6.x     | Authentification *session-based* (non-JWT)                |


## Setup with Intellij IDE
1. Create project from Initializr: File > New > project > Spring Initializr
2. Add lib repository into pom.xml
3. Add folders
    - Source root: src/main/java
    - View: src/main/resources
    - Static: src/main/resource/static
4. Create database with name "demo" as configuration in application.properties
5. Run sql script to create table doc/data.sql

## Implement a Feature
1. Create mapping domain class and place in package com.nnk.springboot.domain
2. Create repository class and place in package com.nnk.springboot.repositories
3. Create controller class and place in package com.nnk.springboot.controllers

## Security
1. Create user service to load user from  database and place in package com.nnk.springboot.services
2. Add configuration class and place in package com.nnk.springboot.config

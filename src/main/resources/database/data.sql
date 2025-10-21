-- ===============================================
-- Jeu de données initial pour la table users
-- Les mots de passe sont déjà hachés avec BCrypt :
--    - "admin" → mot de passe : admin (haché)
--    - "user"  → mot de passe : user (haché)
-- Ces comptes servent uniquement pour le développement local et la démo.
-- Ils ne sont pas utilisés dans l'environnement de test automatisé.
-- ===============================================

insert into users(fullname, username, password, role)
values("Administrator", "admin", "$2a$12$IFMtLs5EJBx7OHDS.NT9V.N1UUne4scxE5QDkDQ9sfb2rXQlP3a1i", "ADMIN");
insert into users(fullname, username, password, role)
values("User", "user", "$2a$12$QQJDp4HysO3sRboNJXaUZuU4bEXnTjj7OLsGP0YPzOFTHUCRshoDe", "USER");
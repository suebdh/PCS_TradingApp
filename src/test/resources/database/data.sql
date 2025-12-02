-- =====================================================================
-- Jeu de données initial (ENVIRONNEMENT TEST)
--
-- Deux comptes applicatifs minimaux sont créés :
--   • username = admin  (role ADMIN)
--   • username = user   (role USER)
--
-- Les mots de passe sont déjà hachés en BCrypt et leur valeur réelle n'est PAS exposée dans ce fichier.
--
-- IMPORTANT :
-- Ce fichier est utilisé UNIQUEMENT dans l'environnement TEST.
-- L'environnement DEV possède son propre data.sql distinct.
-- =====================================================================
truncate table users ;

insert into users(fullname, username, password, role)
values("Administrator", "admin", "$2a$15$LjCcKMcSnwMYFjLgXApkfub5UioHqpF4wknxPGV98JwlUCTWxydea", "ADMIN");
insert into users(fullname, username, password, role)
values("User", "user", "$2a$15$l3YOUR6OejSiijPYchJ5huwkJyyoUBuQR9roc1B76jGEGF32XHu7m", "USER");
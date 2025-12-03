-- =====================================================================
-- Jeu de données initial (ENVIRONNEMENT DEV)
--
-- Deux comptes applicatifs basiques sont créés pour permettre de se connecter dès le premier lancement de l'application :
--
--   • username = admin  (role ADMIN)
--   • username = user   (role USER)
--
-- Les mots de passe ci-dessous sont *déjà hachés en BCrypt*.
-- Leur valeur réelle n'est PAS exposée dans ce fichier mais uniquement dans la documentation interne
--
-- IMPORTANT :
-- Ce fichier est utilisé UNIQUEMENT dans l'environnement DEV.
-- L'environnement TEST utilise un data.sql séparé.
-- =====================================================================

insert into users(fullname, username, password, role)
values("Administrator", "admin", "$2a$15$yXmYdZrsrHePWXjikOx3EujPPQI.YNgupgdCvsPOhZVGIR5Mfk/xe", "ADMIN");
insert into users(fullname, username, password, role)
values("User", "user", "$2a$15$4I9Hsjx.kwLK8T/2wEJcoueyfu193Yi67n2qWGD0U/UL0PN/nv5Fy", "USER");
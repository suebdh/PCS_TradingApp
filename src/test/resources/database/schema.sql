-- Suppression des tables existantes pour garantir un schéma propre
-- IMPORTANT : si des relations de clé étrangère sont ajoutées plus tard, toujours supprimer d'abord les tables "enfants" (celles qui
-- contiennent les FOREIGN KEY) avant les tables "parents" (référencées), afin d'éviter les erreurs de contrainte.
DROP TABLE IF EXISTS `bid_list`;
DROP TABLE IF EXISTS `curve_point`;
DROP TABLE IF EXISTS `rating`;
DROP TABLE IF EXISTS `trade`;
DROP TABLE IF EXISTS `rule_name`;
DROP TABLE IF EXISTS `users`;

-- Création des tables
CREATE TABLE bid_list (
  bid_list_id INT NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  bid_quantity INT UNSIGNED,
  ask_quantity INT UNSIGNED,
  bid DECIMAL(10,4) UNSIGNED,
  ask DECIMAL(10,4) UNSIGNED,
  benchmark VARCHAR(125),
  bid_list_date TIMESTAMP,
  commentary VARCHAR(125),
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  book VARCHAR(125),
  creation_name VARCHAR(125),
  creation_date TIMESTAMP ,
  revision_name VARCHAR(125),
  revision_date TIMESTAMP ,
  deal_name VARCHAR(125),
  deal_type VARCHAR(125),
  source_list_id VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (bid_list_id )
);

CREATE TABLE trade (
  trade_id INT NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  buy_quantity DOUBLE,
  sell_quantity DOUBLE,
  buy_price DOUBLE ,
  sell_price DOUBLE,
  trade_date TIMESTAMP,
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  benchmark VARCHAR(125),
  book VARCHAR(125),
  creation_name VARCHAR(125),
  creation_date TIMESTAMP ,
  revision_name VARCHAR(125),
  revision_date TIMESTAMP ,
  deal_name VARCHAR(125),
  deal_type VARCHAR(125),
  source_list_id VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (trade_id)
);

CREATE TABLE curve_point (
  curve_point_id INT NOT NULL AUTO_INCREMENT,
  curve_id INT,
  as_of_date TIMESTAMP,
  -- Maturité (en années fractionnaires) et valeur associée (taux ou rendement)
  term DECIMAL(10,4),
  value DECIMAL(10,4),
  creation_date TIMESTAMP ,

  PRIMARY KEY (curve_point_id)
);

CREATE TABLE rating (
  rating_id INT NOT NULL AUTO_INCREMENT,
  moodys_rating VARCHAR(125),
  sand_p_rating VARCHAR(125),
  fitch_rating VARCHAR(125),
  order_number INT,

  PRIMARY KEY (rating_id)
);

CREATE TABLE rule_name (
  rule_name_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sql_str VARCHAR(125),
  sql_part VARCHAR(125),

  PRIMARY KEY (rule_name_id)
);

CREATE TABLE users (
  user_id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(125),
  password VARCHAR(125),
  fullname VARCHAR(125),
  role VARCHAR(125),

  PRIMARY KEY (user_id)
);

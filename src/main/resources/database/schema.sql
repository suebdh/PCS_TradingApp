-- ============================================================================
-- SCHEMA SQL - ENVIRONNEMENT DEV
-- ----------------------------------------------------------------------------
-- Exécuté uniquement lors du premier démarrage (spring.sql.init.mode=always).
-- En DEV, la base ne doit PAS être écrasée à chaque lancement.
--
-- Par conséquent :
--   - "CREATE TABLE IF NOT EXISTS" est utilisé → évite les erreurs si la table existe
--   - La contrainte UNIQUE est définie directement dans le CREATE (plus simple)
--
-- Objectif : initialiser la base une seule fois sans perturber les données existantes lors des redémarrages en développement.
-- ============================================================================

CREATE TABLE IF NOT EXISTS bid_list (
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

CREATE TABLE IF NOT EXISTS trade (
  trade_id INT NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  -- Quantités à 2 décimales
  buy_quantity DECIMAL(10,2) UNSIGNED,
  sell_quantity DECIMAL(10,2) UNSIGNED,
   -- Prix à 4 décimales
  buy_price DECIMAL(10,4) UNSIGNED,
  sell_price DECIMAL(10,4) UNSIGNED,
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

CREATE TABLE IF NOT EXISTS curve_point (
  curve_point_id INT NOT NULL AUTO_INCREMENT,
  curve_id INT,
  as_of_date TIMESTAMP,
    -- Maturité (en années fractionnaires) et valeur associée (taux ou rendement)
    term DECIMAL(10,4),
    value DECIMAL(10,4),
  creation_date TIMESTAMP ,

  PRIMARY KEY (curve_point_id)
);

CREATE TABLE IF NOT EXISTS rating (
  rating_id INT NOT NULL AUTO_INCREMENT,
  moodys_rating VARCHAR(125),
  sand_p_rating VARCHAR(125),
  fitch_rating VARCHAR(125),
  order_number INT,

  PRIMARY KEY (rating_id)
);

CREATE TABLE IF NOT EXISTS rule (
  rule_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sql_str VARCHAR(125),
  sql_part VARCHAR(125),

  PRIMARY KEY (rule_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(125) NOT NULL,
  password VARCHAR(125) NOT NULL,
  fullname VARCHAR(125) NOT NULL,
  role VARCHAR(125) NOT NULL,

  PRIMARY KEY (user_id),
  UNIQUE INDEX `username_UNIQUE` (username)
);


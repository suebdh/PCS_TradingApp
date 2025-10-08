package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entité représentant une offre (Bid) soumise sur la plateforme Poseidon Capital Solutions.
 * Chaque instance correspond à une proposition d'achat ou de vente effectuée par un utilisateur, contenant des informations telles que le compte, le type, le benchmark, la quantité, et le taux.
 * L'annotation @Required (Spring Framework) a été supprimée, car elle est obsolète depuis Spring Framework 5.1 (Spring Boot 2.1)
 * et supprimée dans Spring Framework 6 (Spring Boot 3.x, qui embarque cette version).
 * Elle n'était pas utile dans cette entité JPA, qui ne dépend pas de l'injection Spring.
 */

@Entity
@Table(name = "bidlist")
public class BidList {
    // TODO: Map columns in data table BIDLIST with corresponding java fields
}

package com.poseidon.tradingapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Entité représentant une offre (Bid) soumise sur la plateforme Poseidon Capital Solutions.
 * Chaque instance correspond à une proposition d'achat ou de vente effectuée par un utilisateur, contenant des informations telles que le compte, le type, le benchmark, la quantité, et le taux.
 * L'annotation @Required (Spring Framework) a été supprimée, car elle est obsolète depuis Spring Framework 5.1 (Spring Boot 2.1)
 * et supprimée dans Spring Framework 6 (Spring Boot 3.x, qui embarque cette version).
 * Elle n'était pas utile dans cette entité JPA, qui ne dépend pas de l'injection Spring.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bidlist")
public class BidList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidListId;

    String account;
    String type;
    Double bidQuantity;
    Double askQuantity;
    Double bid;
    Double ask;
    String benchmark;
    Timestamp bidListDate;
    String commentary;
    String security;
    String status;
    String trader;
    String book;
    String creationName;
    Timestamp creationDate;
    String revisionName;
    Timestamp revisionDate;
    String dealName;
    String dealType;
    String sourceListId;
    String side;

    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}

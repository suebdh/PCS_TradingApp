package com.poseidon.tradingapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "bid_list")
public class BidList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidListId;

    @Column(nullable = false, length = 30)
    private String account;
    @Column(nullable = false, length = 30)
    private String type;

    @Positive
    private Integer bidQuantity;
    @Positive
    private Integer askQuantity;
    @DecimalMin("0.0")
    @Column(precision = 10, scale = 4)
    private BigDecimal bid;
    @DecimalMin("0.0")
    @Column(precision = 10, scale = 4)
    private BigDecimal ask;
    @Column(length = 125)
    private String benchmark;
    private LocalDateTime bidListDate;
    @Column(length = 125)
    private String commentary;
    @Column(length = 125)
    private String security;
    @Column(length = 10)
    private String status;
    @Column(length = 125)
    private String trader;
    @Column(length = 125)
    private String book;
    @Column(length = 125)
    private String creationName;
    private LocalDateTime creationDate;
    @Column(length = 125)
    private String revisionName;
    private LocalDateTime revisionDate;
    @Column(length = 125)
    private String dealName;
    @Column(length = 125)
    private String dealType;
    @Column(length = 125)
    private String sourceListId;
    @Column(length = 125)
    private String side;
}

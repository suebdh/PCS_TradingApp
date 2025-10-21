package com.poseidon.tradingapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant une transaction (Trade) dans l'application Poseidon Capital Solutions.
 * Chaque enregistrement correspond à une opération financière (achat/vente) effectuée par un trader.
 * <p>
 * Contient les informations sur le compte, le type de transaction, les quantités, les prix,
 * ainsi que les métadonnées (dates, statut, source, etc.).
 */
@Getter
@Setter
@NoArgsConstructor // constructeur vide, obligatoire pour JPA
@AllArgsConstructor // constructeur complet
@Entity
@Table(name = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tradeId;

    @Column(nullable = false, length = 30)
    private String account;
    @Column(nullable = false, length = 30)
    private String type;
    // Quantités avec 2 décimales (ex : 100.50)
    @DecimalMin("0.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal buyQuantity;
    @DecimalMin("0.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal sellQuantity;
    // Prix avec 4 décimales (ex : 12.3456)
    @DecimalMin("0.0000")
    @Column(precision = 10, scale = 4)
    private BigDecimal buyPrice;
    @DecimalMin("0.0000")
    @Column(precision = 10, scale = 4)
    private BigDecimal sellPrice;
    private LocalDateTime tradeDate;
    @Column(length = 125)
    private String security;
    @Column(length = 10)
    private String status;
    @Column(length = 125)
    private String trader;
    @Column(length = 125)
    private String benchmark;
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

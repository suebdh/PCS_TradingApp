package com.poseidon.tradingapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Double buyQuantity;
    private Double sellQuantity;
    private Double buyPrice;
    private Double sellPrice;
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

    // Constructeur pratique utilisé dans les tests
    public Trade(String tradeAccount, String type) {
        this.account = tradeAccount;
        this.type = type;
    }
}

package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Entité représentant une transaction (Trade) dans l'application Poseidon Capital Solutions.
 * Chaque enregistrement correspond à une opération financière (achat/vente) effectuée par un trader.
 *
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
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer tradeId;
    private String account;
    private String type;
    private Double buyQuantity;
    private Double sellQuantity;
    private Double buyPrice;
    private Double sellPrice;
    private String benchmark;
    private Timestamp tradeDate;
    private String security;
    private String status;
    private String trader;
    private String book;
    private String creationName;
    private Timestamp creationDate;
    private String revisionName;
    private Timestamp revisionDate;
    private String dealName;
    private String dealType;
    private String sourceListId;
    private String side;

    // Constructeur pratique utilisé dans les tests
    public Trade(String tradeAccount, String type) {
        this.account = tradeAccount;
        this.type = type;
    }
}

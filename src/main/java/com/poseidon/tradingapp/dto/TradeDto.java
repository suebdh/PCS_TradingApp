package com.poseidon.tradingapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) pour la gestion des transactions (Trade).
 * Sert à transférer et valider les données saisies dans les formulaires avant de les mapper vers l'entité {@link com.poseidon.tradingapp.domain.Trade}.
 */
@Getter
@Setter
@NoArgsConstructor
public class TradeDto {

    // === CHAMPS PRINCIPAUX (obligatoires) ===
    private Integer tradeId; // Ajout nécessaire pour Thymeleaf et les routes d'update/delete

    @NotBlank(message = "Le compte (account) est obligatoire.")
    @Size(max = 30, message = "Le compte ne doit pas dépasser 30 caractères.")
    private String account;

    @NotBlank(message = "Le type de transaction est obligatoire.")
    @Size(max = 30, message = "Le type ne doit pas dépasser 30 caractères.")
    private String type;

    @DecimalMin(value = "0.00", message = "La quantité d'achat doit être positive ou nulle.")
    private BigDecimal buyQuantity;

    @DecimalMin(value = "0.00", message = "La quantité de vente doit être positive ou nulle.")
    private BigDecimal sellQuantity;

    @DecimalMin(value = "0.0000", message = "Le prix d'achat doit être positif ou nul.")
    private BigDecimal buyPrice;

    @DecimalMin(value = "0.0000", message = "Le prix de vente doit être positif ou nul.")
    private BigDecimal sellPrice;

    // === CHAMPS SUPPLÉMENTAIRES ===

    private LocalDateTime tradeDate;

    @Size(max = 125, message = "Le champ security ne doit pas dépasser 125 caractères.")
    private String security;

    @Size(max = 10, message = "Le champ status ne doit pas dépasser 10 caractères.")
    private String status;

    @Size(max = 125, message = "Le champ trader ne doit pas dépasser 125 caractères.")
    private String trader;

    @Size(max = 125, message = "Le benchmark ne doit pas dépasser 125 caractères.")
    private String benchmark;

    @Size(max = 125, message = "Le champ book ne doit pas dépasser 125 caractères.")
    private String book;

    @Size(max = 125, message = "Le champ creationName ne doit pas dépasser 125 caractères.")
    private String creationName;

    private LocalDateTime creationDate;

    @Size(max = 125, message = "Le champ revisionName ne doit pas dépasser 125 caractères.")
    private String revisionName;

    private LocalDateTime revisionDate;

    @Size(max = 125, message = "Le champ dealName ne doit pas dépasser 125 caractères.")
    private String dealName;

    @Size(max = 125, message = "Le champ dealType ne doit pas dépasser 125 caractères.")
    private String dealType;

    @Size(max = 125, message = "Le champ sourceListId ne doit pas dépasser 125 caractères.")
    private String sourceListId;

    @Size(max = 125, message = "Le champ side ne doit pas dépasser 125 caractères.")
    private String side;
}

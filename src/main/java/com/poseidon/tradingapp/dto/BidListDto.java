package com.poseidon.tradingapp.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BidListDto {
    // === IDENTIFIANT ===
    private Integer bidListId;

    // === CHAMPS PRINCIPAUX (obligatoires) ===
    @NotBlank(message = "Le compte est obligatoire")
    @Size(max = 30, message = "Le compte ne peut pas dépasser 30 caractères")
    private String account;

    @NotBlank(message = "Le type est obligatoire")
    @Size(max = 30, message = "Le type ne peut pas dépasser 30 caractères")
    private String type;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être strictement positive")
    private Integer bidQuantity;

    // === CHAMPS OPTIONNELS pour une extension plus tard si besoin ===
     @Positive(message = "La quantité doit être strictement positive")
     private Integer askQuantity;

     @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif")
     @Digits(integer = 10, fraction = 4, message = "Max 10 chiffres et 4 décimales")
     private BigDecimal bid;

     @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif")
     @Digits(integer = 10, fraction = 4, message = "Max 10 chiffres et 4 décimales")
     private BigDecimal ask;

    @Size(max = 125, message = "Le benchmark ne peut pas dépasser 125 caractères")
    private String benchmark;

    private LocalDateTime bidListDate;

    @Size(max = 125, message = "Le commentaire ne peut pas dépasser 125 caractères")
    private String commentary;

    @Size(max = 125, message = "Le nom du titre financier (action, obligation, etc.) ne peut pas dépasser 125 caractères")
    private String security;

    @Size(max = 10, message = "Le statut ne peut pas dépasser 10 caractères")
    private String status;

    @Size(max = 125, message = "Le nom du trader ne peut pas dépasser 125 caractères")
    private String trader;

    @Size(max = 125, message = "Le nom du book ne peut pas dépasser 125 caractères")
    private String book;

    @Size(max = 125, message = "Le nom du créateur ne peut pas dépasser 125 caractères")
    private String creationName;

    private LocalDateTime creationDate;

    @Size(max = 125, message = "Le nom du réviseur ne peut pas dépasser 125 caractères")
    private String revisionName;

    private LocalDateTime revisionDate;

    @Size(max = 125, message = "Le nom de la transaction ne peut pas dépasser 125 caractères")
    private String dealName;

    @Size(max = 125, message = "Le type de la transaction ne peut pas dépasser 125 caractères")
    private String dealType;

    @Size(max = 125, message = "L'identifiant source ne peut pas dépasser 125 caractères")
    private String sourceListId;

    @Size(max = 125, message = "Le side ou sens de l'offre (achat/vente) ne peut pas dépasser 125 caractères")
    private String side;

}

package com.poseidon.tradingapp.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingDto {
    // === IDENTIFIANT ===
    /**
     * Identifiant unique du Rating.
     * Correspond à la clé primaire générée automatiquement en base.
     */
    private Integer ratingId;
    // === CHAMPS PRINCIPAUX (obligatoires) de la notation ===
    @Size(max = 125, message = "Le moodysRating ne peut pas dépasser 125 caractères")
    private String moodysRating;
    @Size(max = 125, message = "Le sandPRating ne peut pas dépasser 125 caractères")
    private String sandPRating;
    @Size(max = 125, message = "Le fitchRating ne peut pas dépasser 125 caractères")
    private String fitchRating;
    // === CHAMPS OPTIONNELS pour une extension plus tard si besoin ===
    //Ordre d'affichage ou d'utilisation de la notation
    private Integer orderNumber;
}

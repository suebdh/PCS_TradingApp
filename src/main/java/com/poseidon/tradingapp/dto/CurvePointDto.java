package com.poseidon.tradingapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CurvePointDto {

    // === IDENTIFIANT ===
    /**
     * Identifiant unique du CurvePoint.
     * Correspond à la clé primaire générée automatiquement en base.
     */
    private Integer curvePointId;

    // === CHAMPS PRINCIPAUX (obligatoires) ===
    /**
     * Maturité du point de courbe, exprimée en années.
     * <p>
     * Exemples :
     * - 1.00 → 1 an
     * - 0.25 → 3 mois
     * - 2.5833 → 2 ans et 7 mois
     * <p>
     * Ce champ représente l'échéance ("term") du point sur la courbe de taux.
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Le terme 'term' doit être positif")
    @Digits(integer = 10, fraction = 4, message = "Max 10 chiffres et 4 décimales")
    private BigDecimal term;

    /**
     * Valeur associée à ce point de courbe.
     * Généralement un taux (ex : 0.0173 = 1,73%).
     * <p>
     * Ce champ représente le rendement, taux ou valeur d'intérêt liée à la maturité 'term'.
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "La valeur 'value' doit être positive")
    @Digits(integer = 10, fraction = 4, message = "Max 10 chiffres et 4 décimales")
    private BigDecimal value;

    // === CHAMPS OPTIONNELS pour une extension plus tard si besoin ===
    /**
     * Identifiant de la courbe à laquelle appartient ce point.
     * Chaque courbe (par exemple une courbe des taux en EUR, USD, etc.) est composée de plusieurs CurvePoints.
     * Exemples :
     * - 1 → courbe de taux EUR
     * - 2 → courbe de taux USD
     */
    private Integer curveId;
    /**
     * Date effective de la donnée de marché.
     * Représente la date à laquelle le point de courbe est valide ("as of").
     * <p>
     * Exemple : SI la donnée provient du marché du 14/02/2025 ALORS asOfDate = 2025-02-14
     * <p>
     * Cette date peut être renseignée automatiquement (now) ou fournie par l'utilisateur.
     */
    private LocalDateTime asOfDate;
    /**
     * Date de création du point dans l'application.
     * Utilisée pour tracer l'historique et suivre l'activité.
     * <p>
     * Contrairement à asOfDate (date "métier"), creationDate est une date purement "technique".
     * Généralement définie automatiquement lors de la création du CurvePoint.
     */
    private LocalDateTime creationDate;
}

package com.poseidon.tradingapp.domain;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant un point de courbe (CurvePoint) utilisé dans la gestion des courbes de taux.
 * Chaque enregistrement correspond à un point défini par une courbe, une valeur et une date.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "curve_point")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer curvePointId;

    private Integer curveId;
    private LocalDateTime asOfDate;

    /**
     * Chaque CurvePoint représente un point de la courbe de taux
     * - term : maturité du point (en années fractionnaires, ex : 1.0417 = 1 an et 15 jours, 3 mois → 0.25 an, 2 ans + 7 mois → 2.5833 ans).
     * - value : taux ou rendement associé à cette maturité (ex : 1,73 % → 0.0173, 0,25 % → 0.0025)
     */
    @DecimalMin("0.0")
    @Column(precision = 10, scale = 4)
    private BigDecimal term;
    @DecimalMin("0.0")
    @Column(precision = 10, scale = 4)
    private BigDecimal value;

    private LocalDateTime creationDate;
}

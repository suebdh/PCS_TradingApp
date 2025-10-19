package com.poseidon.tradingapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

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
    private Double term;
    private Double value;
    private LocalDateTime creationDate;
}

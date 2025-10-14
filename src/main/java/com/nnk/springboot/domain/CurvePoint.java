package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Entité représentant un point de courbe (CurvePoint) utilisé dans la gestion des courbes de taux.
 * Chaque enregistrement correspond à un point défini par une courbe, une valeur et une date.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer curveId;
    private Timestamp asOfDate;
    private Double term;
    private Double value;
    private Timestamp creationDate;

    /**
     * Constructeur pratique pour les tests et initialisations rapides.
     * @param curveId identifiant de la courbe
     * @param term la maturité ou la durée
     * @param value la valeur associée à cette maturité
     */
    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}

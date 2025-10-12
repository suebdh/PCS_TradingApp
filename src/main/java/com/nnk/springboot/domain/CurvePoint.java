package com.nnk.springboot.domain;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Entité représentant un point de courbe (CurvePoint) utilisé dans la gestion des courbes de taux.
 * Chaque enregistrement correspond à un point défini par une courbe, une valeur et une date.
 */
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

}

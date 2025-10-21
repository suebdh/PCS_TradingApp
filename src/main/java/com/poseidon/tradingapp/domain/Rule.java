package com.poseidon.tradingapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité représentant une règle de validation (Rule)
 * utilisée dans l'application Poseidon Capital Solutions.
 * Chaque règle contient un nom, une description et des expressions 'SQL/JSON' associées.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rule")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ruleId;

    @Column(length = 125)
    private String name;
    @Column(length = 125)
    private String description;
    @Column(length = 125)
    private String json;
    @Column(length = 512)
    private String template;
    @Column(length = 125)
    private String sqlStr;
    @Column(length = 125)
    private String sqlPart;
}

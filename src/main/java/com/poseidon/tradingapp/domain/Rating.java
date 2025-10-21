package com.poseidon.tradingapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité représentant la notation (Rating) attribuée à un instrument financier.
 * Chaque enregistrement contient les différentes notations selon les agences Moody's, S&P et Fitch.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ratingId;
    @Column(length = 125)
    private String moodysRating;
    @Column(name = "sand_p_rating", length = 125)
    private String sandPRating;
    @Column(length = 125)
    private String fitchRating;
    private Integer orderNumber;
}

package com.poseidon.tradingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) pour la classe Rule
 * Sert à valider et transférer les données entre la vue et la couche service sans exposer directement l'entité JPA.
 */
@Getter
@Setter
@NoArgsConstructor
public class RuleDto {
    private Integer ruleId;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 125, message = "Le nom de la règle ne peut pas dépasser 125 caractères")
    private String name;
    @Size(max = 125, message = "La description de la règle ne peut pas dépasser 125 caractères")
    private String description;
    @Size(max = 125, message = "Le champ JSON ne peut pas dépasser 125 caractères")
    private String json;
    @Size(max = 512, message = "Le template ne peut pas dépasser 512 caractères")
    private String template;
    @Size(max = 125, message = "La requête SQL ne peut pas dépasser 125 caractères.")
    private String sqlStr;
    @Size(max = 125, message = "Le fragment SQL ne peut pas dépasser 125 caractères.")
    private String sqlPart;
}

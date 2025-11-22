package com.poseidon.tradingapp.mappers;

import com.poseidon.tradingapp.domain.Rule;
import com.poseidon.tradingapp.dto.RuleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper MapStruct pour la conversion entre Rule (entité JPA) et RuleDto (objet de transfert de données).
 * <p>
 * Géré automatiquement par MapStruct en générant une implémentation (RuleMapperImpl.java) à la compilation,
 * effectuant ainsi un mapping champ à champ
 */
@Mapper(componentModel = "spring") // permet l'injection Spring @Autowired
public interface RuleMapper {

    /**
     * Convertit une entité Rule en DTO.
     */
    RuleDto toDto (Rule entity);

    /**
     * Convertit un DTO en entité CurvePoint.
     * L'ID est ignoré pour laisser JPA gérer sa génération.
     */
    @Mapping(target = "ruleId", ignore = true)
    Rule toEntity (RuleDto dto);

    /**
     * Met à jour une entité existante à partir d'un DTO.
     * L'ID est ignoré pour éviter tout écrasement.
     */
    @Mapping(target = "ruleId", ignore = true) // on ignore l'ID pour ne pas l'écraser
    void updateEntityFromDto(RuleDto dto, @MappingTarget Rule entity); //@MappingTarget pour dire ce n'est pas un objet à créer, mais un objet existant à mettre à jour.
}

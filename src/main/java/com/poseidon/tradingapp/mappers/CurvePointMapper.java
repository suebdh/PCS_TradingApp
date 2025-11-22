package com.poseidon.tradingapp.mappers;

import com.poseidon.tradingapp.domain.CurvePoint;
import com.poseidon.tradingapp.dto.CurvePointDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
// Indique à MapStruct de générer une implémentation du mapper en tant que bean Spring (@Component),
// ce qui permet son injection automatique via @Autowired ou par constructeur.
public interface CurvePointMapper {
    /**
     * Convertit une entité CurvePoint en DTO.
     */
    CurvePointDto toDto (CurvePoint entity);

    /**
     * Convertit un DTO en entité CurvePoint.
     * L'ID est ignoré pour laisser JPA gérer sa génération.
     */
    @Mapping(target = "curvePointId", ignore = true)
    CurvePoint toEntity (CurvePointDto dto);

    /**
     * Met à jour une entité existante à partir d'un DTO.
     * L'ID est ignoré pour éviter tout écrasement.
     */
    @Mapping(target = "curvePointId", ignore = true)// on ignore l'ID pour éviter de le remplacer
    void updateEntityFromDto(CurvePointDto dto, @MappingTarget CurvePoint entity);
}

package com.poseidon.tradingapp.mappers;

import com.poseidon.tradingapp.domain.Trade;
import com.poseidon.tradingapp.dto.TradeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper MapStruct pour la conversion entre Trade (entité JPA)
 * et TradeDto (objet de transfert de données).
 * <p>
 * MapStruct génère automatiquement l'implémentation de cette interface
 * (TradeMapperImpl.java) à la compilation, pour un mapping champ à champ performant.
 */
@Mapper(componentModel = "spring") // permet l'injection via @Autowired
public interface TradeMapper {
    /**
     * Convertit une entité Trade en DTO.
     *
     * @param entity l'entité Trade
     * @return le DTO correspondant
     */
    TradeDto toDto(Trade entity);

    /**
     * Convertit un DTO en entité Trade.
     * L'ID est ignoré pour laisser JPA gérer sa génération.
     *
     * @param dto le TradeDto
     * @return l'entité Trade correspondante
     */
    @Mapping(target = "tradeId", ignore = true)
    Trade toEntity(TradeDto dto);

    /**
     * Met à jour une entité existante à partir d'un TradeDto.
     * <p>
     * Cette méthode est utile pour les opérations de mise à jour :
     * elle copie les champs du DTO dans une entité déjà persistée.
     */
    @Mapping(target = "tradeId", ignore = true) // on ignore l'ID pour éviter de le remplacer
    void updateEntityFromDto(TradeDto dto, @MappingTarget Trade entity);
}

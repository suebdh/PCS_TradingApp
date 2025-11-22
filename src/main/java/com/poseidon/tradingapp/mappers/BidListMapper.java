package com.poseidon.tradingapp.mappers;

import com.poseidon.tradingapp.domain.BidList;
import com.poseidon.tradingapp.dto.BidListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
// Indique à MapStruct de générer une implémentation du mapper en tant que bean Spring (@Component),
// ce qui permet son injection automatique via @Autowired ou par constructeur.
public interface BidListMapper {
    /**
     * Convertit une entité {@link BidList} en son équivalent {@link BidListDto}.
     *
     * @param entity l'entité à convertir
     * @return le DTO correspondant
     */
    BidListDto toDto(BidList entity);

    /**
     * Convertit un DTO {@link BidListDto} en entité {@link BidList}.
     * L'identifiant bidListId est ignoré : il sera généré automatiquement par JPA.
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    @Mapping(target = "bidListId", ignore = true)
    BidList toEntity(BidListDto dto);

    /**
     * Met à jour une entité existante à partir d'un DTO.
     * L'identifiant {@code bidListId} est ignoré pour éviter tout écrasement.
     *
     * @param dto    source de données (DTO)
     * @param entity entité cible à mettre à jour
     */
    @Mapping(target = "bidListId", ignore = true)
    // on ignore l'ID pour éviter de le remplacer
    void updateEntityFromDto(BidListDto dto, @MappingTarget BidList entity);
}

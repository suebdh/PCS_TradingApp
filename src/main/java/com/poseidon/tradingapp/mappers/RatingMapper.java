package com.poseidon.tradingapp.mappers;

import com.poseidon.tradingapp.domain.Rating;
import com.poseidon.tradingapp.dto.RatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
// Indique à MapStruct de générer une implémentation du mapper en tant que bean Spring (@Component),
// ce qui permet son injection automatique via @Autowired ou par constructeur.
public interface RatingMapper {

    RatingDto toDto (Rating entity);

    @Mapping(target = "ratingId", ignore = true) // Empêche le DTO d'imposer un ID à la création CAR JPA gère la génération de l'ID
    Rating toEntity (RatingDto dto);

    @Mapping(target = "ratingId", ignore = true)// Ne jamais écraser l'ID existant lors d'un update
    void updateEntityFromDto(RatingDto dto, @MappingTarget Rating entity);
}

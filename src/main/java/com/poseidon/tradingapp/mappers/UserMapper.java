package com.poseidon.tradingapp.mappers;

import com.poseidon.tradingapp.domain.User;
import com.poseidon.tradingapp.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
// Indique à MapStruct de générer une implémentation du mapper en tant que bean Spring (@Component),
// ce qui permet son injection automatique via @Autowired ou par constructeur.
public interface UserMapper {
    UserDto toDto (User entity);

    @Mapping(target = "userId", ignore = true) // Empêche le DTO d'imposer un ID à la création CAR JPA gère la génération de l'ID
    User toEntity (UserDto dto);

    @Mapping(target = "userId", ignore = true)// Ne jamais écraser l'ID existant lors d'un update
    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);
}

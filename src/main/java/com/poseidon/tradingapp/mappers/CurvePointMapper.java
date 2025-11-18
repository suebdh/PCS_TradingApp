package com.poseidon.tradingapp.mappers;

import com.poseidon.tradingapp.domain.CurvePoint;
import com.poseidon.tradingapp.dto.CurvePointDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CurvePointMapper {
    CurvePoint toEntity (CurvePointDto dto);
    CurvePointDto toDto (CurvePoint entity);

    @Mapping(target = "curvePointId", ignore = true)// on ignore l'ID pour éviter de le remplacer
    void updateEntityFromDto(CurvePointDto dto, @MappingTarget CurvePoint entity);
}

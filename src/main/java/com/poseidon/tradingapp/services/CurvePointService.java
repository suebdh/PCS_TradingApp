package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.CurvePoint;
import com.poseidon.tradingapp.dto.CurvePointDto;
import com.poseidon.tradingapp.exceptions.CurvePointNotFoundException;
import com.poseidon.tradingapp.mappers.CurvePointMapper;
import com.poseidon.tradingapp.repositories.CurvePointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service métier permettant de gérer les opérations CRUD sur les points de courbe (CurvePoint).
 * Assure la conversion entité ↔ DTO via MapStruct et la gestion des exceptions métier.
 */
@Slf4j
@Service
public class CurvePointService {

    public final CurvePointRepository curvePointRepository;
    public final CurvePointMapper curvePointMapper;

    public CurvePointService(CurvePointRepository curvePointRepository, CurvePointMapper curvePointMapper) {
        this.curvePointRepository = curvePointRepository;
        this.curvePointMapper = curvePointMapper;
    }

    // ==========================
    // FIND ALL
    // ==========================
    public List<CurvePointDto> findAll() {
        return curvePointRepository.findAll()
                .stream()
                .map(curvePointMapper::toDto)
                .toList();
    }

    // ==========================
    // FIND BY ID
    // ==========================
    public CurvePointDto findById(Integer id) {
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> new CurvePointNotFoundException(
                        "CurvePoint introuvable avec id = " + id
                ));

        return curvePointMapper.toDto(entity);
    }

    // ==========================
    // CREATE
    // ==========================
    public CurvePointDto create(CurvePointDto dto) {
        CurvePoint entity = curvePointMapper.toEntity(dto);
        entity.setCreationDate(LocalDateTime.now());
        entity.setAsOfDate(LocalDateTime.now());

        CurvePoint saved = curvePointRepository.save(entity);
        return curvePointMapper.toDto(saved);
    }

    // ==========================
    // UPDATE
    // ==========================
    public CurvePointDto update(Integer id, CurvePointDto dto) {
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> new CurvePointNotFoundException(
                        "Impossible de mettre à jour : CurvePoint id=" + id + " introuvable."
                ));

        curvePointMapper.updateEntityFromDto(dto, entity);
        CurvePoint updated = curvePointRepository.save(entity);

        return curvePointMapper.toDto(updated);
    }

    // ==========================
    // DELETE
    // ==========================
    public void delete(Integer id) {
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> new CurvePointNotFoundException(
                        "Impossible de supprimer : CurvePoint id=" + id + " introuvable."
                ));

        curvePointRepository.delete(entity);
    }

}

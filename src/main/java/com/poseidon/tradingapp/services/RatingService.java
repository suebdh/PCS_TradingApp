package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.Rating;
import com.poseidon.tradingapp.dto.RatingDto;
import com.poseidon.tradingapp.exceptions.RatingNotFoundException;
import com.poseidon.tradingapp.mappers.RatingMapper;
import com.poseidon.tradingapp.repositories.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RatingService {

    public final RatingRepository ratingRepository;
    public final RatingMapper ratingMapper;

    public RatingService(RatingRepository ratingRepository, RatingMapper ratingMapper) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    // ==========================
    // FIND ALL
    // ==========================
    public List<RatingDto> findAll() {
        return ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toDto)
                .toList();
    }

    // ==========================
    // FIND BY ID
    // ==========================
    public RatingDto findById(Integer id) {
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException("Rating(notation) introuvable avec id = " + id));

        return ratingMapper.toDto(entity);
    }

    // ==========================
    // CREATE
    // ==========================
    public RatingDto create(RatingDto dto) {

        Rating entity = ratingMapper.toEntity(dto);
        Rating saved = ratingRepository.save(entity);

        log.info("Rating créé avec ID={}", saved.getRatingId());
        return ratingMapper.toDto(saved);
    }

    // ==========================
    // UPDATE
    // ==========================
    public RatingDto update(Integer id, RatingDto dto) {
        log.info("Mise à jour du Rating id={}", id);
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(
                        "Impossible de mettre à jour : Rating id=" + id + " introuvable."
                ));

        ratingMapper.updateEntityFromDto(dto, entity);
        Rating updated = ratingRepository.save(entity);

        log.info("Rating id={} mis à jour avec succès", id);
        return ratingMapper.toDto(updated);
    }

    // ==========================
    // DELETE
    // ==========================
    public void delete(Integer id) {
        log.info("Suppression du Rating id={}", id);
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(
                        "Impossible de supprimer : Rating id=" + id + " introuvable."
                ));

        ratingRepository.delete(entity);
        log.info("Rating id={} supprimé avec succès", id);
    }
}

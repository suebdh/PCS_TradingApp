package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.Trade;
import com.poseidon.tradingapp.dto.TradeDto;
import com.poseidon.tradingapp.exceptions.TradeNotFoundException;
import com.poseidon.tradingapp.mappers.TradeMapper;
import com.poseidon.tradingapp.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier pour la gestion des transactions (Trade)
 * Contient la logique de validation et la conversion entre DTO et entité via MapStruct
 */
@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeMapper tradeMapper;

    public TradeService(TradeRepository tradeRepository, TradeMapper tradeMapper) {
        this.tradeRepository = tradeRepository;
        this.tradeMapper = tradeMapper;
    }

    /**
     * Récupère la liste complète des trades.
     * @return liste de tous les TradeDto
     */
    public List<TradeDto> findAll() {

        return tradeRepository.findAll()
                .stream()
                .map(tradeMapper::toDto)
                .toList();
    }

    /**
     * Recherche un Trade par son ID.
     * @param id identifiant du Trade
     * @return TradeDto correspondant
     * @throws TradeNotFoundException si l'id est introuvable
     */
    public TradeDto findById(Integer id) {

        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException("Trade non trouvé avec l'id : " + id));
        return tradeMapper.toDto(trade);
    }

    /**
     * Crée un nouveau Trade dans la base de données à partir d'un DTO.
     * @param dto données du trade à sauvegarder
     * @return TradeDto sauvegardé
     */
    public TradeDto create(TradeDto dto) {
        Trade entity = tradeMapper.toEntity(dto);
        Trade saved = tradeRepository.save(entity);
        return tradeMapper.toDto(saved);
    }

    /**
     * Met à jour un Trade existant.
     * @param id identifiant du Trade à mettre à jour
     * @param dto nouvelles données à appliquer
     * @return TradeDto mis à jour
     * @throws TradeNotFoundException si l'id n'existe pas
     */
    public TradeDto update(Integer id, TradeDto dto) {
        Trade existingTrade = tradeRepository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException("Mise à jour impossible : Trade introuvable avec l'id : " + id));

        // Copie les champs du DTO vers l'entité existante
        tradeMapper.updateEntityFromDto(dto, existingTrade);

        Trade updated = tradeRepository.save(existingTrade);
        return tradeMapper.toDto(updated);
    }

    /**
     * Supprime un Trade par son ID.
     * @param id identifiant du Trade à supprimer
     * @throws TradeNotFoundException si l'id n'existe pas
     */
    public void delete(Integer id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException("Suppression impossible : Trade introuvable avec l'id : " + id));
        tradeRepository.delete(trade);
    }

}

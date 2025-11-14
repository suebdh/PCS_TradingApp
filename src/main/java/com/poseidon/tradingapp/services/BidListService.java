package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.BidList;
import com.poseidon.tradingapp.dto.BidListDto;
import com.poseidon.tradingapp.exceptions.BidListNotFoundException;
import com.poseidon.tradingapp.mappers.BidListMapper;
import com.poseidon.tradingapp.repositories.BidListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier pour la gestion des offres (BidList)
 * Contient la logique de validation et la conversion entre DTO et entité via MapStruct
 */
@Service
public class BidListService {

    private final BidListRepository bidListRepository;
    private final BidListMapper bidListMapper;

    /**
     * Constructeur du service BidList.
     *
     * @param bidListRepository le repository JPA pour accéder aux données BidList
     * @param bidListMapper     le mapper MapStruct pour convertir entre entité et DTO
     */
    public BidListService(BidListRepository bidListRepository, BidListMapper bidListMapper) {
        this.bidListRepository = bidListRepository;
        this.bidListMapper = bidListMapper;
    }
    /**
     * Récupère la liste complète des offres enregistrées.
     *
     * @return une liste de {@link BidListDto} représentant toutes les offres
     */
    public List<BidListDto> findAll() {
        return bidListRepository.findAll()
                .stream()
                .map(bidListMapper::toDto)
                .toList();
    }

    /**
     * Récupère une offre à partir de son identifiant unique.
     *
     * @param id identifiant de l'offre à rechercher
     * @return l'offre correspondante sous forme de {@link BidListDto}
     * @throws BidListNotFoundException si l'offre n'existe pas
     */
    public BidListDto findById(Integer id ){
        BidList bidList = bidListRepository.findById(id)
                .orElseThrow( ()-> new BidListNotFoundException("BidList introuvable pour l'id : " + id));
        return bidListMapper.toDto(bidList);
    }

    /**
     * Crée une nouvelle offre en base de données.
     *
     * @param bidListDto données de l'offre à créer
     * @return l'offre créée sous forme de {@link BidListDto}
     */
    public BidListDto create(BidListDto bidListDto){
        BidList entity = bidListMapper.toEntity(bidListDto);
        BidList saved = bidListRepository.save(entity);
        return bidListMapper.toDto(saved);
    }

    /**
     * Met à jour une offre existante à partir de son identifiant.
     *
     * @param id  identifiant de l'offre à mettre à jour
     * @param dto nouvelles données à appliquer
     * @return l'offre mise à jour sous forme de {@link BidListDto}
     * @throws BidListNotFoundException si l'offre n'existe pas
     */
    public BidListDto update (Integer id, BidListDto dto){
        BidList existingBidList = bidListRepository.findById(id)
                .orElseThrow(() -> new BidListNotFoundException("Mise à jour impossible : Offre introuvable avec l'id : " + id));

        // Copie les champs du DTO vers l'entité existante
        bidListMapper.updateEntityFromDto(dto, existingBidList);

        BidList updated = bidListRepository.save(existingBidList);
        return bidListMapper.toDto(updated);

    }

    /**
     * Supprime une offre existante à partir de son identifiant.
     *
     * @param id identifiant de l'offre à supprimer
     * @throws BidListNotFoundException si l'offre n'existe pas
     */
    public void delete(Integer id){
        BidList bidList = bidListRepository.findById(id)
                .orElseThrow(() -> new BidListNotFoundException("Suppression impossible : Offre introuvable avec l'id : " + id));
        bidListRepository.delete(bidList);
    }
}

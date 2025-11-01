package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.Trade;
import com.poseidon.tradingapp.exceptions.TradeNotFoundException;
import com.poseidon.tradingapp.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier pour la gestion des transactions (Trade)
 * Contient la logique de manipulation et de validation avant interaction avec la base de données
 */
@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    /**
     * Récupère la liste complète des trades.
     * @return liste de tous les Trade
     */
    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    /**
     * Recherche un Trade par son ID.
     * @param id identifiant du Trade
     * @return Trade correspondant
     * @throws TradeNotFoundException si l'id est introuvable
     */
    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException("Trade non trouvé avec l'id : " + id));
    }

    /**
     * Crée un nouveau Trade dans la base de données.
     * @param trade objet Trade à sauvegarder
     * @return Trade sauvegardé
     */
    public Trade save(Trade trade) {
        //TODO vérifier les doublons ou appliquer d'autres règles métiers
        return tradeRepository.save(trade);
    }

    /**
     * Met à jour un Trade existant.
     * @param id identifiant du Trade à mettre à jour
     * @param trade données à appliquer
     * @return Trade mis à jour
     * @throws TradeNotFoundException si l'id n'existe pas
     */
    public Trade update(Integer id, Trade trade) {
        Trade existingTrade = findById(id); // lève une exception si introuvable
        trade.setTradeId(existingTrade.getTradeId());
        return tradeRepository.save(trade);
    }

    /**
     * Supprime un Trade par son ID.
     * @param id identifiant du Trade à supprimer
     * @throws TradeNotFoundException si l'id n'existe pas
     */
    public void delete(Integer id) {
        Trade existingTrade = findById(id);
        tradeRepository.delete(existingTrade);
    }

}

package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.Rule;
import com.poseidon.tradingapp.dto.RuleDto;
import com.poseidon.tradingapp.exceptions.RuleAlreadyExistsException;
import com.poseidon.tradingapp.exceptions.RuleNotFoundException;
import com.poseidon.tradingapp.mappers.RuleMapper;
import com.poseidon.tradingapp.repositories.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des règles (Rule)
 * Contient la logique de validation, de vérification de doublons et de conversion DTO - Entité via MapStruct
 */
@Service
public class RuleService {
    private final RuleRepository ruleRepository;
    private final RuleMapper ruleMapper;

    public RuleService (RuleRepository ruleRepository, RuleMapper ruleMapper){
        this.ruleRepository=ruleRepository;
        this.ruleMapper = ruleMapper;
    }

    /**
     * Crée une nouvelle règle à partir d'un DTO.
     * Vérifie que le nom n'existe pas déjà avant d'enregistrer.
     */
    public RuleDto createRule(RuleDto dto) {
        // vérification des doublons
        if (ruleRepository.existsByName(dto.getName())){
            throw new RuleAlreadyExistsException("Une règle porte le même nom : " + dto.getName());
        }
        // Sauvegarde l'entité et retourne le DTO créé
        Rule saved = ruleRepository.save(ruleMapper.toEntity(dto));
        return ruleMapper.toDto(saved);
    }

    /**
     * Récupère toutes les règles existantes.
     */
    public List<RuleDto> getAllRules() {

        return ruleRepository.findAll()
                .stream()
                .map(ruleMapper::toDto)
                .toList();
    }

    /**
     * Récupère une règle par son ID.
     */
    public RuleDto getRuleById(Integer id) {
        Optional<Rule> ruleOpt = ruleRepository.findById(id);
        if (ruleOpt.isEmpty()) {
            throw new RuleNotFoundException("Impossible d'afficher la règle : ID " + id);
        }
        return ruleMapper.toDto(ruleOpt.get());
    }

    /**
     * Met à jour une règle existante.
     */
    public RuleDto updateRule(Integer id, RuleDto dto) {
        Rule existingRule = ruleRepository.findById(id)
                .orElseThrow(() -> new RuleNotFoundException("Mise à jour impossible : aucune règle trouvée avec l'ID " + id));

        // Vérifie que le nom n'appartient pas déjà à une autre règle
        if (!existingRule.getName().equals(dto.getName()) && ruleRepository.existsByName(dto.getName())) {
            throw new RuleAlreadyExistsException("Une autre règle porte déjà le nom : " + dto.getName());
        }

        // Met à jour les champs de l'entité existante à partir du DTO (MapStruct gère le mapping champ par champ automatiquement)
        ruleMapper.updateEntityFromDto(dto, existingRule);

        // Sauvegarde l'entité et retourne le DTO mis à jour
        Rule updated = ruleRepository.save(existingRule);
        return ruleMapper.toDto(updated);
    }

    /**
     * Supprime une règle existante par son ID.
     */
    public void deleteRule(Integer id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new RuleNotFoundException("Suppression impossible : aucune règle trouvée avec l'ID " + id));
        ruleRepository.delete(rule);
    }
}

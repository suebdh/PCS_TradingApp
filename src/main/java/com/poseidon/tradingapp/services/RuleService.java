package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.Rule;
import com.poseidon.tradingapp.dto.RuleDto;
import com.poseidon.tradingapp.exceptions.RuleAlreadyExistsException;
import com.poseidon.tradingapp.exceptions.RuleNotFoundException;
import com.poseidon.tradingapp.repositories.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des règles (Rule)
 * Contient la logique de validation, de vérification de doublons et de conversion DTO - Entité.
 */
@Service
public class RuleService {
    private final RuleRepository ruleRepository;

    public RuleService (RuleRepository ruleRepository){
        this.ruleRepository=ruleRepository;
    }

    /**
     * Crée une nouvelle règle à partir d'un DTO.
     * Vérifie que le nom n'existe pas déjà avant d'enregistrer.
     */
    public Rule createRule(RuleDto dto) {
        // vérification des doublons
        if (ruleRepository.existsByName(dto.getName())){
            throw new RuleAlreadyExistsException("Une règle porte le même nom : " + dto.getName());
        }
        // conversion DTO → Entity
        Rule rule = convertToEntity(dto);
        // sauvegarde en base
        return ruleRepository.save(rule);
    }

    /**
     * Récupère toutes les règles existantes.
     */
    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    public RuleDto getRuleById(Integer id) {
        Optional<Rule> ruleOpt = ruleRepository.findById(id);
        if (ruleOpt.isEmpty()) {
            throw new RuleNotFoundException("Aucune règle trouvée avec l'ID " + id);
        }
        return convertToDto(ruleOpt.get());
    }

    /**
     * Met à jour une règle existante.
     */
    public Rule updateRule(Integer id, RuleDto dto) {
        Rule existingRule = ruleRepository.findById(id)
                .orElseThrow(() -> new RuleNotFoundException("Aucune règle trouvée avec l'ID " + id));

        // Vérifie que le nom n'appartient pas déjà à une autre règle
        if (!existingRule.getName().equals(dto.getName()) && ruleRepository.existsByName(dto.getName())) {
            throw new RuleAlreadyExistsException("Une autre règle porte déjà le nom : " + dto.getName());
        }

        // Met à jour les champs
        existingRule.setName(dto.getName());
        existingRule.setDescription(dto.getDescription());
        existingRule.setJson(dto.getJson());
        existingRule.setTemplate(dto.getTemplate());
        existingRule.setSqlStr(dto.getSqlStr());
        existingRule.setSqlPart(dto.getSqlPart());

        // Sauvegarde et retourne l'entité mise à jour
        return ruleRepository.save(existingRule);
    }

    /**
     * Supprime une règle existante par son ID.
     */
    public void deleteRule(Integer id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new RuleNotFoundException("Aucune règle trouvée avec l'ID " + id));
        ruleRepository.delete(rule);
    }

    /**
     * Convertit un RuleDto vers une entité Rule.
     */
    private Rule convertToEntity(RuleDto dto) {
        Rule entity = new Rule();
        entity.setRuleId(dto.getRuleId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setJson(dto.getJson());
        entity.setTemplate(dto.getTemplate());
        entity.setSqlStr(dto.getSqlStr());
        entity.setSqlPart(dto.getSqlPart());
        return entity;
    }

    /**
     * Convertit une entité Rule vers un RuleDto.
     */
    private RuleDto convertToDto(Rule entity) {
        RuleDto dto = new RuleDto();
        dto.setRuleId(entity.getRuleId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setJson(entity.getJson());
        dto.setTemplate(entity.getTemplate());
        dto.setSqlStr(entity.getSqlStr());
        dto.setSqlPart(entity.getSqlPart());
        return dto;
    }
}

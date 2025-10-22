package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.Rule;
import com.poseidon.tradingapp.dto.RuleDto;
import com.poseidon.tradingapp.exceptions.RuleAlreadyExistsException;
import com.poseidon.tradingapp.repositories.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return dto;
    }
}

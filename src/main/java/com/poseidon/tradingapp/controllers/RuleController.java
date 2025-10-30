package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.dto.RuleDto;
import com.poseidon.tradingapp.exceptions.RuleAlreadyExistsException;
import com.poseidon.tradingapp.exceptions.RuleNotFoundException;
import com.poseidon.tradingapp.services.RuleService;
import com.poseidon.tradingapp.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * Affiche la liste des règles existantes.
     */
    @GetMapping("/rule/list")
    public String home(Model model) {
        log.info("GET /rule/list - Récupération de la liste des règles");
        model.addAttribute("rules", ruleService.getAllRules());
        return "rule/list";
    }

    /**
     * Affiche le formulaire d'ajout d'une règle.
     */
    @GetMapping("/rule/add")
    public String showAddForm(Model model) {
        log.info("GET /rule/add - Affichage du formulaire d'ajout de règle");
        model.addAttribute("rule", new RuleDto());
        return "rule/add";
    }

    /**
     * Valide le formulaire et crée une nouvelle règle.
     */
    @PostMapping("/rule/validate")
    public String validate(@Valid @ModelAttribute("rule") RuleDto ruleDto, BindingResult result, RedirectAttributes redirectAttributes) {
        log.debug("POST /rule/validate - Données reçues : {}", ruleDto);

        // Si des champs obligatoires sont manquants ou invalides, on reste sur la page d'ajout de règle
        if (result.hasErrors()) {
            log.warn("Validation échouée pour la création de règle : {}", ruleDto.getName());
            return "rule/add";
        }

        // Enregistre la règle dans la base
        try {
            ruleService.createRule(ruleDto);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.RULE_ADD_SUCCESS);
            log.info("Règle ajoutée avec succès : {}", ruleDto.getName());
        } catch (RuleAlreadyExistsException e) {
            // Message utilisateur clair + message technique pour le dev
            log.warn("Création refusée (doublon) : {}", e.getMessage());
            result.rejectValue("name", "error.rule", MessageUtils.RULE_DUPLICATE);
            return "rule/add";
        }

        // Redirige vers la page liste
        return "redirect:/rule/list";
    }

    @GetMapping("/rule/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        log.info("GET /rule/update/{} - Chargement du formulaire d'édition", id);
        try {
            RuleDto ruleDto = ruleService.getRuleById(id);
            model.addAttribute("rule", ruleDto);
            return "rule/update";
        } catch (RuleNotFoundException e) {
            // Si la règle n'existe pas → redirection vers la liste avec un message simple
            log.warn("Accès édition impossible (id inexistant) : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.RULE_EDIT_NOT_FOUND);
            return "redirect:/rule/list";
        }

    }

    @PostMapping("/rule/update/{id}")
    public String updateRule(@PathVariable("id") Integer id, @Valid @ModelAttribute("rule") RuleDto ruleDto,
                             BindingResult result, RedirectAttributes redirectAttributes) {
        log.debug("POST /rule/update/{} - Données reçues : {}", id, ruleDto);
        if (result.hasErrors()) {
            log.warn("Validation échouée pour la mise à jour de la règle id={}", id);
            // Si des erreurs de validation, on reste sur la page d'édition de la règle
            return "rule/update";
        }

        try {
            ruleService.updateRule(id, ruleDto);
            redirectAttributes.addFlashAttribute("successMessage",MessageUtils.RULE_UPDATE_SUCCESS);
            log.info("Règle mise à jour avec succès : id={}, nom={}", id, ruleDto.getName());
            return "redirect:/rule/list";
        } catch (RuleNotFoundException e) {
            log.warn("Mise à jour impossible (id inexistant) : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.RULE_UPDATE_NOT_FOUND);
            return "redirect:/rule/list";
        } catch (RuleAlreadyExistsException e) {
            log.warn("Mise à jour refusée (doublon) : {}", e.getMessage());
            result.rejectValue("name", "error.rule", MessageUtils.RULE_DUPLICATE);
            return "rule/update";
        }
    }

    @GetMapping("/rule/delete/{id}")
    public String deleteRule(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        log.info("GET /rule/delete/{} - Suppression d'une règle", id);
        try {
            ruleService.deleteRule(id);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.RULE_DELETE_SUCCESS);
            log.info("Règle supprimée avec succès : id={}", id);
        } catch (RuleNotFoundException e) {
            log.warn("Suppression impossible (id inexistant) : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.RULE_DELETE_NOT_FOUND);
        }
        return "redirect:/rule/list";
    }
}

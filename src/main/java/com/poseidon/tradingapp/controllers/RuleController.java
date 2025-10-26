package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.dto.RuleDto;
import com.poseidon.tradingapp.exceptions.RuleAlreadyExistsException;
import com.poseidon.tradingapp.exceptions.RuleNotFoundException;
import com.poseidon.tradingapp.services.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RuleService ruleService;

    /**
     * Affiche la liste des règles existantes.
     */
    @GetMapping("/rule/list")
    public String home(Model model) {
        model.addAttribute("rules", ruleService.getAllRules());
        return "rule/list";
    }

    /**
     * Affiche le formulaire d'ajout d'une règle.
     */
    @GetMapping("/rule/add")
    public String showAddForm(Model model) {
        model.addAttribute("rule", new RuleDto());
        return "rule/add";
    }

    /**
     * Valide le formulaire et crée une nouvelle règle.
     */
    @PostMapping("/rule/validate")
    public String validate(@Valid @ModelAttribute("rule") RuleDto ruleDto, BindingResult result, RedirectAttributes redirectAttributes) {
        // Si des champs obligatoires sont manquants ou invalides, on reste sur la page d'ajout de règle
        if (result.hasErrors()) {
            return "rule/add";
        }

        // Enregistre la règle dans la base
        try {
            ruleService.createRule(ruleDto);
            redirectAttributes.addFlashAttribute("successMessage", "La règle a été ajoutée avec succès !");
        } catch (RuleAlreadyExistsException e) {
            // Message utilisateur clair + message technique pour le dev
            result.rejectValue("name", "error.rule", "Une règle avec ce nom existe déjà");
            log.warn("Création refusée (doublon) : {}", e.getMessage());
            return "rule/add";
        }

        // Redirige vers la page liste
        return "redirect:/rule/list";
    }

    @GetMapping("/rule/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            RuleDto ruleDto = ruleService.getRuleById(id);
            model.addAttribute("rule", ruleDto);
            return "rule/update";
        } catch (RuleNotFoundException e) {
            // Si la règle n'existe pas → redirection vers la liste avec un message simple
            log.warn("Accès edition impossible (id inexistant) : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "La règle demandée pour édition n'existe pas ou a été supprimée");
            return "redirect:/rule/list";
        }

    }

    @PostMapping("/rule/update/{id}")
    public String updateRule(@PathVariable("id") Integer id, @Valid @ModelAttribute("rule") RuleDto ruleDto,
                             BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Si des erreurs de validation, on reste sur la page d'édition de la règle
            return "rule/update";
        }

        try {
            ruleService.updateRule(id, ruleDto);
            redirectAttributes.addFlashAttribute("successMessage","La règle a été mise à jour avec succès !");
            return "redirect:/rule/list";
        } catch (RuleNotFoundException e) {
            log.warn("Mise à jour impossible (id inexistant) : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Mise à jour impossible : La règle n'existe pas");
            return "redirect:/rule/list";
        } catch (RuleAlreadyExistsException e) {
            log.warn("Mise à jour refusée (doublon) : {}", e.getMessage());
            result.rejectValue("name", "error.rule", "Une règle avec ce nom existe déjà");
            return "rule/update";
        }
    }

    @GetMapping("/rule/delete/{id}")
    public String deleteRule(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            ruleService.deleteRule(id);
            redirectAttributes.addFlashAttribute("successMessage", "La règle a été supprimée avec succès !");
        } catch (RuleNotFoundException e) {
            log.warn("Suppression impossible (id inexistant) : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Suppression impossible : la règle n'existe pas");
        }
        return "redirect:/rule/list";
    }
}

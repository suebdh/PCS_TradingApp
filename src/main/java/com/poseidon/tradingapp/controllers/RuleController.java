package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.dto.RuleDto;
import com.poseidon.tradingapp.exceptions.RuleAlreadyExistsException;
import com.poseidon.tradingapp.exceptions.RuleNotFoundException;
import com.poseidon.tradingapp.services.RuleService;
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
            // Si une règle du même nom existe déjà, on affiche un message d'erreur
            result.rejectValue("name", "error.rule", e.getMessage());
            return "rule/add";
        }

        // Redirige vers la page liste
        return "redirect:/rule/list";
    }

    @GetMapping("/rule/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            RuleDto ruleDto = ruleService.getRuleById(id);
            model.addAttribute("rule", ruleDto);
            return "rule/update";
        } catch (RuleNotFoundException e) {
            // Si la règle n'existe pas → redirection vers la liste avec un message simple
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/rule/list";
        }

    }

    @PostMapping("/rule/update/{id}")
    public String updateRule(@PathVariable("id") Integer id, @Valid @ModelAttribute("rule") RuleDto ruleDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si des erreurs de validation, on reste sur la page d'édition de la règle
            return "rule/update";
        }

        try {
            ruleService.updateRule(id, ruleDto);
        } catch (RuleNotFoundException e) {
            result.rejectValue("name", "error.rule", e.getMessage());
            return "rule/update";
        } catch (RuleAlreadyExistsException e) {
            result.rejectValue("name", "error.rule", e.getMessage());
            return "rule/update";
        }

        return "redirect:/rule/list";
    }

    @GetMapping("/rule/delete/{id}")
    public String deleteRule(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            ruleService.deleteRule(id);
            redirectAttributes.addFlashAttribute("successMessage", "La règle a été supprimée avec succès !");
        } catch (RuleNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/rule/list";
    }
}

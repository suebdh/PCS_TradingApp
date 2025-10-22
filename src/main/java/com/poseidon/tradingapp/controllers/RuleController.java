package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.Rule;
import com.poseidon.tradingapp.dto.RuleDto;
import com.poseidon.tradingapp.exceptions.RuleAlreadyExistsException;
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
    public String validate(@Valid @ModelAttribute("rule") RuleDto ruleDto, BindingResult result, Model model) {
        // Si des champs obligatoires sont manquants ou invalides, on reste sur la page d'ajout de règle
        if (result.hasErrors()) {
            return "rule/add";
        }

        // Enregistre la règle dans la base
        try {
            ruleService.createRule(ruleDto);
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
        Rule dummy = new Rule();
        dummy.setRuleId(id);
        dummy.setName("TEST Rule " + id);
        model.addAttribute("rule", dummy);
        return "rule/update";
    }

    @PostMapping("/rule/update/{id}")
    public String updateRule(@PathVariable("id") Integer id, @Valid Rule rule,
                             BindingResult result, Model model) {
        return "redirect:/rule/list";
    }

    @GetMapping("/rule/delete/{id}")
    public String deleteRule(@PathVariable("id") Integer id, Model model) {
        return "redirect:/rule/list";
    }
}

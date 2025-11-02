package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.Trade;
import com.poseidon.tradingapp.exceptions.TradeNotFoundException;
import com.poseidon.tradingapp.services.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {

        this.tradeService = tradeService;
    }

    /**
     * Affiche la liste de tous les trades
     */
    @GetMapping("/list")
    public String home(Model model)
    {
        log.info("GET /trade/list - Récupération de la liste des trades");
        model.addAttribute("trades", tradeService.findAll());
        return "trade/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau trade
     */
    @GetMapping("/add")
    public String addTradeForm(Model model) {
        log.info("GET /trade/add - Affichage du formulaire d'ajout d'un nouveau trade");
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    /**
     * Valide et enregistre un nouveau trade
     */
    @PostMapping("/validate")
    public String validate(@ModelAttribute("trade") Trade trade,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        log.info("POST /trade/validate - Validation et enregistrement d'un nouveau trade : {}", trade.getAccount());

        if (result.hasErrors()) {
            log.warn("Validation échouée pour le trade : {}", trade);
            return "trade/add";
        }

        tradeService.save(trade);
        redirectAttributes.addFlashAttribute("successMessage", "Trade ajouté avec succès !");
        log.info("Trade ajouté avec succès : {}", trade.getAccount());
        return "redirect:/trade/list";
    }

    /**
     * Affiche le formulaire de mise à jour d'un trade existant
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model,  RedirectAttributes redirectAttributes) {
        log.info("GET /trade/update/{} - Récupération du trade à mettre à jour", id);
        try {
            Trade trade = tradeService.findById(id);
            model.addAttribute("trade", trade);
            return "trade/update";
        } catch (TradeNotFoundException e) {
            log.error("Trade introuvable avec id={}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Trade introuvable !");
            return "redirect:/trade/list";
        }
    }

    /**
     * Met à jour un trade existant après validation du formulaire
     */
    @PostMapping("/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id,
                              @Valid @ModelAttribute("trade") Trade trade,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        log.info("POST /trade/update/{} - Tentative de mise à jour du trade", id);

        if (result.hasErrors()) {
            log.warn("Validation échouée lors de la mise à jour du trade id={}", id);
            return "trade/update";
        }

        try {
            tradeService.update(id, trade);
            redirectAttributes.addFlashAttribute("successMessage", "Trade mis à jour avec succès !");
            log.info("Trade id={} mis à jour avec succès", id);
        } catch (TradeNotFoundException e) {
            log.error("Erreur lors de la mise à jour : Trade id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Mise à jour impossible : Trade introuvable !");
        }

        return "redirect:/trade/list";
    }

    /**
     * Supprime un trade existant
     */
    @GetMapping("/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        log.info("GET /trade/delete/{} - Suppression d'un trade", id);

        try {
            tradeService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Trade supprimé avec succès !");
            log.info("Trade id={} supprimé avec succès", id);
        } catch (TradeNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Suppression impossible : Trade introuvable !");
        }
        return "redirect:/trade/list";
    }
}

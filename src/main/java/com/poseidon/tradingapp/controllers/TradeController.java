package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.dto.TradeDto;
import com.poseidon.tradingapp.exceptions.TradeNotFoundException;
import com.poseidon.tradingapp.services.TradeService;
import com.poseidon.tradingapp.utils.MessageUtils;
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
        model.addAttribute("trades", tradeService.findAll()); // List<TradeDto>
        return "trade/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau trade
     */
    @GetMapping("/add")
    public String addTradeForm(Model model) {
        log.info("GET /trade/add - Affichage du formulaire d'ajout d'un nouveau trade");
        model.addAttribute("trade", new TradeDto()); // attribut modèle = "trade"
        return "trade/add";
    }

    /**
     * Valide et enregistre un nouveau trade
     */
    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeDto tradeDto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        log.info("POST /trade/validate - Validation et enregistrement d'un nouveau trade : {}", tradeDto.getAccount());

        if (result.hasErrors()) {
            log.warn("Validation échouée pour le trade : {}", tradeDto);
            return "trade/add";
        }

        tradeService.create(tradeDto);// <-- DTO
        redirectAttributes.addFlashAttribute("successMessage", MessageUtils.TRADE_ADD_SUCCESS);
        log.info("Trade ajouté avec succès : {}", tradeDto.getAccount());
        return "redirect:/trade/list";
    }

    /**
     * Affiche le formulaire de mise à jour d'un trade existant
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model,  RedirectAttributes redirectAttributes) {
        log.info("GET /trade/update/{} - Récupération du trade à mettre à jour", id);
        try {
            TradeDto tradeDto = tradeService.findById(id);// <-- DTO
            model.addAttribute("trade", tradeDto); // attribut modèle = "trade"
            return "trade/update";
        } catch (TradeNotFoundException e) {
            log.error("Trade introuvable avec id={}", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.TRADE_EDIT_NOT_FOUND);
            return "redirect:/trade/list";
        }
    }

    /**
     * Met à jour un trade existant après validation du formulaire
     */
    @PostMapping("/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id,
                              @Valid @ModelAttribute("trade") TradeDto tradeDto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        log.info("POST /trade/update/{} - Tentative de mise à jour du trade", id);

        if (result.hasErrors()) {
            log.warn("Validation échouée lors de la mise à jour du trade id={}", id);
            return "trade/update";
        }

        try {
            tradeService.update(id, tradeDto);// <-- DTO
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.TRADE_UPDATE_SUCCESS);
            log.info("Trade id={} mis à jour avec succès", id);
        } catch (TradeNotFoundException e) {
            log.error("Erreur lors de la mise à jour : Trade id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.TRADE_UPDATE_NOT_FOUND);
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
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.TRADE_DELETE_SUCCESS);
            log.info("Trade id={} supprimé avec succès", id);
        } catch (TradeNotFoundException e) {
            log.error("Erreur lors de la suppression : Trade id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.TRADE_DELETE_NOT_FOUND);
        }
        return "redirect:/trade/list";
    }
}

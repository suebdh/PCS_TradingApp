package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.dto.BidListDto;
import com.poseidon.tradingapp.exceptions.BidListNotFoundException;
import com.poseidon.tradingapp.services.BidListService;
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
@RequestMapping("/bidList")
public class BidListController {

    private final BidListService bidListService;

    public BidListController(BidListService bidListService) {
        this.bidListService = bidListService;
    }

    /**
     * Affiche la liste de toutes les offres (BidList)
     */
    @GetMapping("/list")
    public String home(Model model)
    {
        log.info("GET /bidList/list - Récupération de la liste des offres");
        model.addAttribute("bidLists", bidListService.findAll());
        log.info("{} offres récupérées et envoyées à la vue", bidListService.findAll().size());
        return "bidList/list";
    }

    /**
     * Affiche le formulaire d'ajout d'une nouvelle offre
     */
    @GetMapping("/add")
    public String addBidForm(Model model) {
        log.info("GET /bidList/add - Affichage du formulaire d'ajout d'une nouvelle offre");
        model.addAttribute("bidList", new BidListDto());
        return "bidList/add";
    }

    /**
     * Valide et sauvegarde une nouvelle offre
     */
    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidListDto bidListDto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        log.info("POST /bidList/validate - Validation et enregistrement d'une nouvelle offre : {}", bidListDto.getAccount());

        if (result.hasErrors()) {
            log.warn("Validation échouée pour l'offre : {}", bidListDto);
            return "bidList/add";
        }

        bidListService.create(bidListDto);
        redirectAttributes.addFlashAttribute("successMessage", MessageUtils.BID_ADD_SUCCESS);
        log.info("Offre ajoutée avec succès : {}", bidListDto.getAccount());
        return "redirect:/bidList/list";
    }

    /**
     * Affiche le formulaire de mise à jour pour une offre existante
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        log.info("GET /bidList/update/{} - Récupération de l'offre à mettre à jour", id);
        try {
            BidListDto bidListDto = bidListService.findById(id);
            model.addAttribute("bidList", bidListDto);
            return "bidList/update";
        } catch (BidListNotFoundException e) {
            log.error("Offre introuvable avec id={}", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.BID_EDIT_NOT_FOUND);
            return "redirect:/bidList/list";
        }
    }

    /**
     * Met à jour une offre existante
     */
    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @Valid @ModelAttribute("bidList") BidListDto bidListDto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        log.info("POST /bidList/update/{} - Tentative de mise à jour de l'offre", id);
        if (result.hasErrors()) {
            log.warn("Validation échouée lors de la mise à jour de l'offre id={}", id);
            return "bidList/update";
        }

        try {
            bidListService.update(id, bidListDto);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.BID_UPDATE_SUCCESS);
            log.info("Offre id={} mise à jour avec succès", id);
        } catch (BidListNotFoundException e) {
            log.error("Erreur lors de la mise à jour : Offre id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.BID_UPDATE_NOT_FOUND);
        }

        return "redirect:/bidList/list";
    }

    /**
     * Supprime une offre existante
     */
    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            bidListService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.BID_DELETE_SUCCESS);
            log.info("Offre id={} supprimée avec succès", id);
        } catch (BidListNotFoundException e) {
            log.error("Erreur lors de la suppression : Offre id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.BID_DELETE_NOT_FOUND);
        }
        return "redirect:/bidList/list";
    }
}

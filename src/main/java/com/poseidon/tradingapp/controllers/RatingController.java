package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.dto.RatingDto;
import com.poseidon.tradingapp.exceptions.RatingNotFoundException;
import com.poseidon.tradingapp.services.RatingService;
import com.poseidon.tradingapp.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/rating")
public class RatingController {
    public final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Affiche la liste de tous les Ratings.
     */
    @GetMapping("/list")
    public String home(Model model)
    {
        log.info("GET /rating/list - Récupération de la liste des notations");
        List<RatingDto> list= ratingService.findAll();
        model.addAttribute("ratings", list);
        log.info("{} notation(s) récupérée(s) et envoyée(s) à la vue", list.size());
        return "rating/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau Rating.
     */
    @GetMapping("/add")
    public String addRatingForm(Model model) {
        log.info("GET /rating/add - Affichage du formulaire d'ajout d'une nouvelle notation");
        model.addAttribute("rating", new RatingDto());
        return "rating/add";
    }

    /**
     * Valide et enregistre un nouveau Rating.
     */
    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDto ratingDto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        log.info("POST /rating/validate - Validation et enregistrement d'une nouvelle notation");

        if (result.hasErrors()) {
            log.warn("Validation échouée pour le Rating : {}", ratingDto);
            return "rating/add";
        }

        ratingService.create(ratingDto);
        redirectAttributes.addFlashAttribute("successMessage", MessageUtils.RATING_ADD_SUCCESS);
        log.info("Rating ajouté avec succès");
        return "redirect:/rating/list";
    }

    /**
     * Affiche le formulaire de mise à jour pour un Rating existant.
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        log.info("GET /rating/update/{} - Récupération du Rating à mettre à jour", id);

        try {
            RatingDto ratingDto = ratingService.findById(id);
            model.addAttribute("rating", ratingDto);
            return "rating/update";
        } catch (RatingNotFoundException e) {
            log.error("Rating introuvable avec id={}", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.RATING_EDIT_NOT_FOUND);
            return "redirect:/rating/list";
        }
    }

    /**
     * Met à jour un Rating existant après validation du formulaire.
     */
    @PostMapping("/update/{id}")
    public String updateRating(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("rating") RatingDto ratingDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        log.info("POST /rating/update/{} - Tentative de mise à jour du Rating", id);

        if (result.hasErrors()) {
            log.warn("Validation échouée lors de la mise à jour du Rating id={}", id);
            return "rating/update";
        }

        try {
            ratingService.update(id, ratingDto);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.RATING_UPDATE_SUCCESS);
            log.info("Rating id={} mis à jour avec succès", id);
        } catch (RatingNotFoundException e) {
            log.error("Erreur lors de la mise à jour : Rating id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.RATING_UPDATE_NOT_FOUND);
        }

        return "redirect:/rating/list";
    }

    /**
     * Supprime un Rating existant.
     */
    @GetMapping("/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        log.info("GET /rating/delete/{} - Suppression d'un Rating", id);

        try {
            ratingService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.RATING_DELETE_SUCCESS);
            log.info("Rating id={} supprimé avec succès", id);
        } catch (RatingNotFoundException e) {
            log.error("Erreur lors de la suppression : Rating id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.RATING_DELETE_NOT_FOUND);
        }

        return "redirect:/rating/list";
    }
}

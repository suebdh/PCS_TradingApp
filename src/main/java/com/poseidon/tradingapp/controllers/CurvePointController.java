package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.CurvePoint;
import com.poseidon.tradingapp.dto.CurvePointDto;
import com.poseidon.tradingapp.exceptions.CurvePointNotFoundException;
import com.poseidon.tradingapp.services.CurvePointService;
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
@RequestMapping("/curvePoint")
public class CurvePointController {

    public final CurvePointService curvePointService;

    public CurvePointController(CurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }

    /**
     * Affiche la liste de tous les CurvePoints.
     */
    @GetMapping("/list")
    public String home(Model model) {
        log.info("GET /curvePoint/list - Récupération de la liste des points de courbe");
        List<CurvePointDto> list = curvePointService.findAll();
        model.addAttribute("curvePoints", list);
        log.info("{} point(s) de courbe récupéré(s) et envoyés à la vue", list.size());
        return "curvePoint/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau CurvePoint.
     */
    @GetMapping("/add")
    public String addCurvePointForm(Model model) {
        log.info("GET /curvePoint/add - Affichage du formulaire d'ajout d'un nouveau point de courbe");
        model.addAttribute("curvePoint", new CurvePointDto());
        return "curvePoint/add";
    }

    /**
     * Valide et enregistre un nouveau CurvePoint.
     */
    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDto curvePointDto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {

        log.info("POST /curvePoint/validate - Validation et enregistrement d'un nouveau point de courbe");

        if (result.hasErrors()) {
            log.warn("Validation échouée pour le CurvePoint : {}", curvePointDto);
            return "curvePoint/add";
        }

        curvePointService.create(curvePointDto);
        redirectAttributes.addFlashAttribute("successMessage", MessageUtils.CURVE_ADD_SUCCESS);
        log.info("CurvePoint ajouté avec succès");
        return "redirect:/curvePoint/list";
    }

    /**
     * Affiche le formulaire de mise à jour pour un CurvePoint existant.
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        log.info("GET /curvePoint/update/{} - Récupération du CurvePoint à mettre à jour", id);

        try {
            CurvePointDto curvePointDto = curvePointService.findById(id);
            model.addAttribute("curvePoint", curvePointDto);
            return "curvePoint/update";
        } catch (CurvePointNotFoundException e) {
            log.error("CurvePoint introuvable avec id={}", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.CURVE_EDIT_NOT_FOUND);
            return "redirect:/curvePoint/list";
        }
    }

    /**
     * Met à jour un CurvePoint existant après validation du formulaire.
     */
    @PostMapping("/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id,
                                   @Valid @ModelAttribute("curvePoint") CurvePointDto curvePointDto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        log.info("POST /curvePoint/update/{} - Tentative de mise à jour du CurvePoint", id);

        if (result.hasErrors()) {
            log.warn("Validation échouée lors de la mise à jour du CurvePoint id={}", id);
            return "curvePoint/update";
        }

        try {
            curvePointService.update(id, curvePointDto);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.CURVE_UPDATE_SUCCESS);
            log.info("CurvePoint id={} mis à jour avec succès", id);
        } catch (CurvePointNotFoundException e) {
            log.error("Erreur lors de la mise à jour : CurvePoint id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.CURVE_UPDATE_NOT_FOUND);
        }

        return "redirect:/curvePoint/list";
    }

    /**
     * Supprime un CurvePoint existant.
     */
    @GetMapping("/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id,
                                   RedirectAttributes redirectAttributes) {
        log.info("GET /curvePoint/delete/{} - Suppression d'un CurvePoint", id);

        try {
            curvePointService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.CURVE_DELETE_SUCCESS);
            log.info("CurvePoint id={} supprimé avec succès", id);
        } catch (CurvePointNotFoundException e) {
            log.error("Erreur lors de la suppression : CurvePoint id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.CURVE_DELETE_NOT_FOUND);
        }

        return "redirect:/curvePoint/list";
    }
}

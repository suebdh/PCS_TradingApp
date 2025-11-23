package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.dto.UserDto;
import com.poseidon.tradingapp.exceptions.UserNotFoundException;
import com.poseidon.tradingapp.services.UserService;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ============================================================
    // LIST
    // ============================================================
    @GetMapping("/list")
    public String home(Model model)
    {
        log.info("GET /user/list - récupération de la liste des utilisateurs");
        List<UserDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "user/list";
    }

    // ============================================================
    // ADD FORM
    // ============================================================
    @GetMapping("/add")
    public String addUser(Model model) {
        log.info("GET /user/add - affichage du formulaire d'ajout utilisateur");
        model.addAttribute("user", new UserDto());
        return "user/add";
    }

    // ============================================================
    // VALIDATE CREATE
    // ============================================================
    @PostMapping("/validate")
    public String validate(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        log.info("POST /user/validate - tentative de création d'un utilisateur");

        if (result.hasErrors()) {
            log.warn("Validation échouée pour : {}", userDto);
            return "user/add";
        }

        userService.create(userDto);
        redirectAttributes.addFlashAttribute("successMessage", MessageUtils.USER_ADD_SUCCESS);

        return "redirect:/user/list";
    }

    // ============================================================
    // UPDATE FORM
    // ============================================================
    @GetMapping("/update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("GET /user/update/{} - ouverture formulaire update", id);

        try {
            UserDto dto = userService.findById(id);
            dto.setPassword(""); // ne jamais renvoyer le hash

            model.addAttribute("user", dto);
            return "user/update";

        } catch (UserNotFoundException e) {
            log.error("Utilisateur introuvable id={}", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.USER_EDIT_NOT_FOUND);
            return "redirect:/user/list";
        }
    }

    // ============================================================
    // VALIDATE UPDATE
    // ============================================================
    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        log.info("POST /user/update/{} - tentative mise à jour user", id);

        if (result.hasErrors()) {
            log.warn("Erreurs de validation lors du update id={}", id);
            return "user/update";
        }

        try {
            userService.update(id, userDto);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.USER_UPDATE_SUCCESS);

        } catch (UserNotFoundException e) {
            log.error("Update impossible : user id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.USER_UPDATE_NOT_FOUND);
        }

        return "redirect:/user/list";
    }

    // ============================================================
    // DELETE
    // ============================================================
    @GetMapping("/delete/{id}")
    public String deleteUser(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {

        log.info("GET /user/delete/{} - suppression utilisateur", id);

        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", MessageUtils.USER_DELETE_SUCCESS);

        } catch (UserNotFoundException e) {
            log.error("Suppression impossible : user id={} introuvable", id);
            redirectAttributes.addFlashAttribute("errorMessage", MessageUtils.USER_DELETE_NOT_FOUND);
        }

        return "redirect:/user/list";
    }
}

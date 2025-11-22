package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.Rating;
import com.poseidon.tradingapp.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", roles = "USER") // Simule un utilisateur connecté
public class RatingControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    void cleanDatabase() {
        ratingRepository.deleteAll();
    }

    // ============================================================
    // GET /rating/list
    // ============================================================
    @Test
    void shouldGetRatingList() throws Exception {
        // given
        Rating rating = new Rating();
        rating.setMoodysRating("Moodys");
        rating.setSandPRating("SandP");
        rating.setFitchRating("Fitch");
        rating.setOrderNumber(10);
        ratingRepository.save(rating);

        // when + then
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", hasSize(1)));
    }

    // ============================================================
    // GET /rating/add
    // ============================================================
    @Test
    void shouldDisplayAddRatingForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    // ============================================================
    // POST /rating/validate (SUCCESS)
    // ============================================================
    @Test
    void shouldValidateAndSaveRating() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "Moodys")
                        .param("sandPRating", "SandP")
                        .param("fitchRating", "Fitch")
                        .param("orderNumber", "10")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attributeExists("successMessage"));

        // Vérifier en base
        assertEquals(1, ratingRepository.count());
    }

    // ============================================================
    // POST /rating/validate (ERROR VALIDATION)
    // ============================================================
    @Test
    void shouldReturnToAddFormOnValidationError() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "") // invalide
                        .param("sandPRating", "SandP")
                        .param("fitchRating", "Fitch")
                        .param("orderNumber", "10")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeHasFieldErrors("rating", "moodysRating"));
    }

    // ============================================================
    // GET /rating/update/{id} (SUCCESS)
    // ============================================================
    @Test
    void shouldDisplayUpdateForm() throws Exception {
        Rating rating = new Rating();
        rating.setMoodysRating("M");
        rating.setSandPRating("S");
        rating.setFitchRating("F");
        rating.setOrderNumber(1);
        Rating saved = ratingRepository.save(rating);

        mockMvc.perform(get("/rating/update/" + saved.getRatingId()))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"));
    }

    // ============================================================
    // GET /rating/update/{id} (NOT FOUND)
    // ============================================================
    @Test
    void shouldRedirectUpdateFormWhenRatingNotFound() throws Exception {
        mockMvc.perform(get("/rating/update/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    // ============================================================
    // POST /rating/update/{id} (SUCCESS)
    // ============================================================
    @Test
    void shouldUpdateRating() throws Exception {
        Rating rating = new Rating();
        rating.setMoodysRating("M");
        rating.setSandPRating("S");
        rating.setFitchRating("F");
        rating.setOrderNumber(1);
        Rating saved = ratingRepository.save(rating);

        mockMvc.perform(post("/rating/update/" + saved.getRatingId())
                        .param("moodysRating", "Updated")
                        .param("sandPRating", "Updated")
                        .param("fitchRating", "Updated")
                        .param("orderNumber", "99")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attributeExists("successMessage"));

        Rating updated = ratingRepository.findById(saved.getRatingId()).orElseThrow();
        assertEquals(99, updated.getOrderNumber());
    }

    // ============================================================
    // POST /rating/update/{id} (ERROR VALIDATION)
    // ============================================================
    @Test
    void shouldReturnToUpdateFormOnValidationError() throws Exception {
        Rating rating = new Rating();
        rating.setMoodysRating("M");
        rating.setSandPRating("S");
        rating.setFitchRating("F");
        rating.setOrderNumber(1);
        Rating saved = ratingRepository.save(rating);

        mockMvc.perform(post("/rating/update/" + saved.getRatingId())
                        .param("moodysRating", "")  // invalide
                        .param("sandPRating", "S")
                        .param("fitchRating", "F")
                        .param("orderNumber", "1")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeHasFieldErrors("rating", "moodysRating"));
    }

    // ============================================================
    // POST /rating/update/{id} (NOT FOUND)
    // ============================================================
    @Test
    void shouldRedirectUpdateWhenRatingNotFound() throws Exception {
        mockMvc.perform(post("/rating/update/999")
                        .param("moodysRating", "A")
                        .param("sandPRating", "B")
                        .param("fitchRating", "C")
                        .param("orderNumber", "3")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    // ============================================================
    // GET /rating/delete/{id} (SUCCESS)
    // ============================================================
    @Test
    void shouldDeleteRating() throws Exception {
        Rating rating = new Rating();
        rating.setMoodysRating("A");
        rating.setSandPRating("B");
        rating.setFitchRating("C");
        rating.setOrderNumber(7);
        Rating saved = ratingRepository.save(rating);

        mockMvc.perform(get("/rating/delete/" + saved.getRatingId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attributeExists("successMessage"));

        assertEquals(0, ratingRepository.count());
    }

    // ============================================================
    // GET /rating/delete/{id} (NOT FOUND)
    // ============================================================
    @Test
    void shouldRedirectDeleteWhenRatingNotFound() throws Exception {
        mockMvc.perform(get("/rating/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}


package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.User;
import com.poseidon.tradingapp.repositories.UserRepository;
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
@WithMockUser(username = "admin", roles = "ADMIN")
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    // ============================================================
    // GET /user/list
    // ============================================================
    @Test
    void shouldGetUserList() throws Exception {
        // given
        User user = new User();
        user.setUsername("Cindy");
        user.setFullname("Cindy Crawford");
        user.setRole("USER");
        user.setPassword("558899"); // utilisé uniquement en base de test, pas pour la validation DTO

        User saved = userRepository.save(user);

        //when + then
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(1)))
        ;
    }

    // ============================================================
    // GET /user/add
    // ============================================================
    @Test
    void shouldShowAddFormUser() throws Exception {
        //when + then
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"))
        ;
    }

    // ============================================================
    // POST user/validate (SUCCESS)
    // ============================================================
    @Test
    void shouldAddUserWhenSuccess() throws Exception {
        // mot de passe conforme à la regex : au moins 8 car., une maj, une min, un chiffre, un spécial
        String strongPassword = "TestUser1!";

        //when + then
        mockMvc.perform(post("/user/validate")
                        .param("fullname", "Jade BH")
                        .param("username", "Jade")
                        .param("role", "USER")
                        .param("password", strongPassword)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(flash().attributeExists("successMessage"))
        ;
    }

    // ============================================================
    // POST user/validate (FAILURE)
    // ============================================================
    @Test
    void shouldNotAddUserWhenError() throws Exception {
        // username vide → validation doit échouer
        String strongPassword = "TestUser1!";

        //when + then
        mockMvc.perform(post("/user/validate")
                        .param("fullname", " Hammar")
                        .param("username", "")// invalide
                        .param("role", "USER")
                        .param("password", strongPassword) // valide, pour isoler l'erreur sur username
                )
                .andExpect(status().isOk()) // retour normal
                .andExpect(view().name("user/add")) // retour au formulaire
                .andExpect(model().attributeExists("user")) // DTO renvoyé à la vue, il contiendra les erreurs
        ;
    }

    // ============================================================
    // GET /user/update/{id} (SUCCESS)
    // ============================================================
    @Test
    void shouldDisplayUpdateUserForm() throws Exception {

        User user = new User();
        user.setUsername("cindy");
        user.setFullname("Cindy Crawford");
        user.setRole("USER");
        user.setPassword("1234"); // ici, on passe par l'entité directement, pas par le DTO

        User saved = userRepository.save(user);

        mockMvc.perform(get("/user/update/" + saved.getUserId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));
    }

    // ============================================================
    // GET /user/update/{id} (NOT FOUND)
    // ============================================================
    @Test
    void shouldRedirectUpdateFormWhenUserNotFound() throws Exception {

        mockMvc.perform(get("/user/update/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    // ============================================================
    // POST /user/update/{id} (SUCCESS)
    // ============================================================
    @Test
    void shouldUpdateUserWhenSuccess() throws Exception {

        User user = new User();
        user.setUsername("cindy");
        user.setFullname("Cindy Crawford");
        user.setRole("USER");
        user.setPassword("1234");
        User saved = userRepository.save(user);

        String strongPassword = "TestUser1!";

        mockMvc.perform(post("/user/update/" + saved.getUserId())
                        .param("username", "Cino")
                        .param("fullname", "Cindy Updated")
                        .param("role", "ADMIN")
                        .param("password", strongPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(flash().attributeExists("successMessage"));

        User updated = userRepository.findById(saved.getUserId()).orElseThrow();
        assertEquals("Cino", updated.getUsername());
        assertEquals("Cindy Updated", updated.getFullname());
        assertEquals("ADMIN", updated.getRole());
    }


    // ============================================================
    // POST /user/update/{id} (ERROR VALIDATION)
    // ============================================================
    @Test
    void shouldReturnToUpdateFormOnValidationError() throws Exception {

        User user = new User();
        user.setUsername("cindy");
        user.setFullname("Cindy");
        user.setRole("USER");
        user.setPassword("1234");
        User saved = userRepository.save(user);

        String strongPassword = "TestUser1!";

        mockMvc.perform(post("/user/update/" + saved.getUserId())
                        .param("username", "") // invalide
                        .param("fullname", "Cindy")
                        .param("role", "USER")
                        .param("password", strongPassword) // valide → l'erreur porte sur username
                )
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));
    }


    // ============================================================
    // POST /user/update/{id} (NOT FOUND)
    // ============================================================
    @Test
    void shouldRedirectUpdateWhenUserNotFound() throws Exception {

        String strongPassword = "TestUser1!";

        mockMvc.perform(post("/user/update/999")
                        .param("username", "test")
                        .param("fullname", "Test Test")
                        .param("role", "USER")
                        .param("password", strongPassword)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    // ============================================================
    // GET /user/delete/{id} (SUCCESS)
    // ============================================================
    @Test
    void shouldDeleteUser() throws Exception {

        User user = new User();
        user.setUsername("cindy");
        user.setFullname("Cindy Crawford");
        user.setRole("USER");
        user.setPassword("1234");
        User saved = userRepository.save(user);

        mockMvc.perform(get("/user/delete/" + saved.getUserId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(flash().attributeExists("successMessage"));

        assertEquals(0, userRepository.count());
    }

    // ============================================================
    // GET /user/delete/{id} (NOT FOUND)
    // ============================================================
    @Test
    void shouldRedirectDeleteWhenUserNotFound() throws Exception {

        mockMvc.perform(get("/user/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}

package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.Trade;
import com.poseidon.tradingapp.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test d'intégration du TradeController.
 * Vérifie la chaîne complète : Controller → Service → Repository → Hibernate → Base MySQL (profil "test").
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", roles = "USER")
public class TradeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;

    @BeforeEach
    void cleanDatabase() {
        tradeRepository.deleteAll();
    }

    // --------------------------------------------------
    // Méthode : home() → GET /trade/list : doit retourner la vue de la liste des trades
    // --------------------------------------------------
    @Test
    void shouldDisplayTradeList() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("Trade Account");
        trade.setType("Type");
        tradeRepository.save(trade);

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", hasSize(1)))
                .andExpect(model().attribute("trades", hasItem(
                        hasProperty("account", is("Trade Account"))
                )));
    }

    // --------------------------------------------------
    // Méthode addTradeForm() → GET /trade/add : doit afficher le formulaire d'ajout d'un trade
    // --------------------------------------------------
    @Test
    void shouldDisplayAddForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    // --------------------------------------------------
    // POST /trade/validate
    // --------------------------------------------------
    // création valide doit rediriger vers /list
    @Test
    void shouldValidateAndCreateTrade() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "Test Account")
                        .param("type", "Test Type")
                        .param("buyQuantity", "10.5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        // Vérifier que le trade a été créé
        assertEquals(1, tradeRepository.findAll().size());
    }

    // création invalide doit retourner la vue d'ajout
    @Test
    void shouldReturnToAddFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "") // invalide
                        .param("type", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasFieldErrors("trade", "account", "type"));
    }

    // --------------------------------------------------
    // GET /trade/update/{id}
    // --------------------------------------------------
    // trade existant doit afficher la vue update
    @Test
    void shouldShowUpdateForm() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("Existing");
        trade.setType("Type");
        trade = tradeRepository.save(trade);

        mockMvc.perform(get("/trade/update/" + trade.getTradeId()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"))
                .andExpect(model().attribute("trade", hasProperty("account", is("Existing"))));
    }

    // id inexistant doit rediriger vers /list
    @Test
    void shouldRedirectWhenTradeNotFoundOnUpdateForm() throws Exception {
        mockMvc.perform(get("/trade/update/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    // --------------------------------------------------
    // POST /trade/update/{id}
    // --------------------------------------------------
    //mise à jour valide doit rediriger vers /list
    @Test
    void shouldUpdateTrade() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("Old");
        trade.setType("Type");
        trade = tradeRepository.save(trade);

        mockMvc.perform(post("/trade/update/" + trade.getTradeId())
                        .param("account", "Updated")
                        .param("type", "Type"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        Trade updated = tradeRepository.findById(trade.getTradeId()).orElseThrow();
        assertEquals("Updated", updated.getAccount());
    }

    //validation échoue, doit retourner la vue update
    @Test
    void shouldReturnToUpdateFormWhenValidationFails() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("Account");
        trade.setType("Type");
        trade = tradeRepository.save(trade);

        mockMvc.perform(post("/trade/update/" + trade.getTradeId())
                        .param("account", "") // invalide
                        .param("type", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeHasFieldErrors("trade", "account", "type"));
    }

    //id inexistant doit rediriger vers /list
    @Test
    void shouldRedirectWhenTradeNotFoundOnUpdate() throws Exception {
        mockMvc.perform(post("/trade/update/9999")
                        .param("account", "Updated")
                        .param("type", "Type"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    // --------------------------------------------------
    // GET /trade/delete/{id}
    // --------------------------------------------------
    //suppression valide doit rediriger vers /list
    @Test
    void shouldDeleteTrade() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("ToDelete");
        trade.setType("Type");
        trade = tradeRepository.save(trade);

        mockMvc.perform(get("/trade/delete/" + trade.getTradeId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        assertTrue(tradeRepository.findAll().isEmpty());
    }

    @Test
    //id inexistant doit rediriger vers /list
    void shouldRedirectWhenTradeNotFoundOnDelete() throws Exception {
        mockMvc.perform(get("/trade/delete/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

}

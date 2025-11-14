package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.BidList;
import com.poseidon.tradingapp.repositories.BidListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test d'intégration du BidListController.
 * Vérifie la chaîne complète : Controller → Service → Repository → Hibernate → Base MySQL (profil "test").
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", roles = "USER")
public class BidListControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListRepository bidListRepository;

    @BeforeEach
    void cleanDatabase() {
        bidListRepository.deleteAll();
    }

    // --------------------------------------------------
    // GET /bidList/list
    // --------------------------------------------------
    @Test
    void shouldDisplayBidList() throws Exception {
        BidList bid = new BidList();
        bid.setAccount("AccountTest");
        bid.setType("TypeTest");
        bid.setBidQuantity(10);
        bidListRepository.save(bid);

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attribute("bidLists", hasSize(1)))
                .andExpect(model().attribute("bidLists", hasItem(
                        hasProperty("account", is("AccountTest"))
                )));
    }

    // --------------------------------------------------
    // GET /bidList/add
    // --------------------------------------------------
    @Test
    void shouldDisplayAddForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    // --------------------------------------------------
    // POST /bidList/validate
    // --------------------------------------------------
    @Test
    void shouldValidateAndCreateBidList() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "AccountTest")
                        .param("type", "TypeTest")
                        .param("bidQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        assertEquals(1, bidListRepository.findAll().size());
    }

    @Test
    void shouldReturnToAddFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "")   // invalide
                        .param("type", "")
                        .param("bidQuantity", "")) // invalide
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasFieldErrors("bidList", "account", "type", "bidQuantity"));
    }

    // --------------------------------------------------
    // GET /bidList/update/{id}
    // --------------------------------------------------
    @Test
    void shouldShowUpdateForm() throws Exception {
        BidList bid = new BidList();
        bid.setAccount("Existing");
        bid.setType("TypeTest");
        bid.setBidQuantity(10);
        bid = bidListRepository.save(bid);

        mockMvc.perform(get("/bidList/update/" + bid.getBidListId()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().attribute("bidList",
                        hasProperty("account", is("Existing"))));
    }

    @Test
    void shouldRedirectWhenBidListNotFoundOnUpdateForm() throws Exception {
        mockMvc.perform(get("/bidList/update/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    // --------------------------------------------------
    // POST /bidList/update/{id}
    // --------------------------------------------------
    @Test
    void shouldUpdateBidList() throws Exception {
        BidList bid = new BidList();
        bid.setAccount("OldValue");
        bid.setType("TypeTest");
        bid.setBidQuantity(10);
        bid = bidListRepository.save(bid);

        mockMvc.perform(post("/bidList/update/" + bid.getBidListId())
                        .param("account", "UpdatedValue")
                        .param("type", "TypeTest")
                        .param("bidQuantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        BidList updated = bidListRepository.findById(bid.getBidListId()).orElseThrow();
        assertEquals("UpdatedValue", updated.getAccount());
        assertEquals(20, updated.getBidQuantity());
    }

    @Test
    void shouldReturnToUpdateFormWhenValidationFails() throws Exception {
        BidList bid = new BidList();
        bid.setAccount("Value");
        bid.setType("TypeTest");
        bid.setBidQuantity(10);
        bid = bidListRepository.save(bid);

        mockMvc.perform(post("/bidList/update/" + bid.getBidListId())
                        .param("account", "")  // invalide
                        .param("type", "")     // invalide
                        .param("bidQuantity", "")) // invalide
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeHasFieldErrors("bidList", "account", "type", "bidQuantity"));
    }

    @Test
    void shouldRedirectWhenBidListNotFoundOnUpdate() throws Exception {
        mockMvc.perform(post("/bidList/update/9999")
                        .param("account", "UpdatedValue")
                        .param("type", "TypeTest")
                        .param("bidQuantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    // --------------------------------------------------
    // GET /bidList/delete/{id}
    // --------------------------------------------------
    @Test
    void shouldDeleteBidList() throws Exception {
        BidList bid = new BidList();
        bid.setAccount("ToDelete");
        bid.setType("TypeTest");
        bid.setBidQuantity(10);
        bid = bidListRepository.save(bid);

        mockMvc.perform(get("/bidList/delete/" + bid.getBidListId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        assertTrue(bidListRepository.findAll().isEmpty());
    }

    @Test
    void shouldRedirectWhenBidListNotFoundOnDelete() throws Exception {
        mockMvc.perform(get("/bidList/delete/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }
}

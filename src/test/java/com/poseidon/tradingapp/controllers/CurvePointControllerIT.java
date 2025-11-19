package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.CurvePoint;
import com.poseidon.tradingapp.repositories.CurvePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test d'intégration du CurvePointController.
 *
 * Vérifie la chaîne complète :
 * Controller → Service → Repository → Hibernate → Base MySQL (profil "test").
 */

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", roles = "USER")
public class CurvePointControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @BeforeEach
    void cleanDatabase() {
        curvePointRepository.deleteAll();
    }

    // --------------------------------------------------
    // GET /curvePoint/list
    // --------------------------------------------------
    @Test
    void shouldDisplayCurvePointList() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setCurveId(1);
        cp.setTerm(new BigDecimal("10.0000"));
        cp.setValue(new BigDecimal("20.0000"));
        curvePointRepository.save(cp);

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", hasSize(1)))
                .andExpect(model().attribute("curvePoints", hasItem(
                        hasProperty("term", is(new BigDecimal("10.0000")))
                )));
    }

     //--------------------------------------------------
     //GET /curvePoint/add
     //--------------------------------------------------
    @Test
    void shouldDisplayAddForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

     //--------------------------------------------------
     //POST /curvePoint/validate
     //--------------------------------------------------
    @Test
    void shouldValidateAndCreateCurvePoint() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("term", "10.5")
                        .param("value", "20.3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        assertEquals(1, curvePointRepository.findAll().size());
    }

    @Test
    void shouldReturnToAddFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("term", "aaa")    // invalide : pas un nombre
                        .param("value", "bbb"))    // invalide : pas un nombre
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "term", "value"));
    }

    // --------------------------------------------------
    // GET /curvePoint/update/{id}
    // --------------------------------------------------
    @Test
    void shouldShowUpdateForm() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setCurveId(1);
        cp.setTerm(new BigDecimal("10.0000"));
        cp.setValue(new BigDecimal("20.0000"));
        cp = curvePointRepository.save(cp);

        mockMvc.perform(get("/curvePoint/update/" + cp.getCurvePointId()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(model().attribute("curvePoint",
                        hasProperty("term", is(new BigDecimal("10.0000")))));
    }

    @Test
    void shouldRedirectWhenCurvePointNotFoundOnUpdateForm() throws Exception {
        mockMvc.perform(get("/curvePoint/update/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    // --------------------------------------------------
    // POST /curvePoint/update/{id}
    // --------------------------------------------------
    @Test
    void shouldUpdateCurvePoint() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setCurveId(1);
        cp.setTerm(new BigDecimal("1.0000"));
        cp.setValue(new BigDecimal("2.0000"));
        cp = curvePointRepository.save(cp);

        mockMvc.perform(post("/curvePoint/update/" + cp.getCurvePointId())
                        .param("term", "9.9999")
                        .param("value", "7.7777"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        CurvePoint updated = curvePointRepository.findById(cp.getCurvePointId()).orElseThrow();
        assertEquals(new BigDecimal("9.9999"), updated.getTerm());
        assertEquals(new BigDecimal("7.7777"), updated.getValue());
    }

    @Test
    void shouldReturnToUpdateFormWhenValidationFails() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setCurveId(1);
        cp.setTerm(new BigDecimal("1.0000"));
        cp.setValue(new BigDecimal("2.0000"));
        cp = curvePointRepository.save(cp);

        mockMvc.perform(post("/curvePoint/update/" + cp.getCurvePointId())
                        .param("term", "aaaa")   // invalide
                        .param("value", "bbbb"))   // invalide
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "term", "value"));
    }

    @Test
    void shouldRedirectWhenCurvePointNotFoundOnUpdate() throws Exception {
        mockMvc.perform(post("/curvePoint/update/9999")
                        .param("term", "10")
                        .param("value", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    // --------------------------------------------------
    // GET /curvePoint/delete/{id}
    // --------------------------------------------------
    @Test
    void shouldDeleteCurvePoint() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setCurveId(1);
        cp.setTerm(new BigDecimal("10.0000"));
        cp.setValue(new BigDecimal("20.0000"));
        cp = curvePointRepository.save(cp);

        mockMvc.perform(get("/curvePoint/delete/" + cp.getCurvePointId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        assertTrue(curvePointRepository.findAll().isEmpty());
    }

    @Test
    void shouldRedirectWhenCurvePointNotFoundOnDelete() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

}

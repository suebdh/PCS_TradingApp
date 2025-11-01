package com.poseidon.tradingapp.controllers;

import com.poseidon.tradingapp.domain.Rule;
import com.poseidon.tradingapp.repositories.RuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test d'intégration du RuleController.
 * Vérifie la chaîne complète : Controller → Service → Repository → Hibernate → Base MySQL (profil "test").
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = "ADMIN") // Simule un utilisateur connecté
public class RuleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleRepository ruleRepository;

    // ============================================================
    // Méthode : home() → GET /rule/list
    // ============================================================

    /**
     * Vérifie que la liste des règles s'affiche correctement.
     * Doit retourner la vue "rule/list" avec l'attribut "rules" dans le modèle.
     */
    @Test
    void shouldDisplayRuleList() throws Exception {
        mockMvc.perform(get("/rule/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/list"))
                .andExpect(model().attributeExists("rules"));
    }

    // ============================================================
    // Méthode : showAddForm() → GET /rule/add
    // ============================================================

    /**
     * Vérifie que le formulaire d'ajout de règle s'affiche.
     * Doit retourner la vue "rule/add" avec un attribut vide "rule".
     */
    @Test
    void shouldShowAddForm() throws Exception {
        mockMvc.perform(get("/rule/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/add"))
                .andExpect(model().attributeExists("rule"));
    }

    // ============================================================
    // Méthode : validate() → POST /rule/validate
    // ============================================================

    /**
     * Vérifie la création d'une règle valide.
     * Doit rediriger vers "/rule/list" et sauvegarder la règle en base.
     */
    @Test
    void shouldCreateRuleSuccessfully() throws Exception {
        // GIVEN — Données d'entrée simulées
        String name = "Test Rule";
        String description = "Sample description";

        // WHEN — Exécution de la requête POST /rule/validate
        mockMvc.perform(post("/rule/validate")
                        .with(csrf())
                        .param("name", name)
                        .param("description", description)
                        .param("json", "{}")
                        .param("template", "Template content")
                        .param("sqlStr", "SELECT * FROM test")
                        .param("sqlPart", "WHERE id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"));

        // THEN — Vérification en base
        Rule created = ruleRepository.findAll()
                .stream()
                .filter(r -> name.equals(r.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("La règle n'a pas été créée en base"));

        assertEquals(description, created.getDescription());
        assertEquals("Template content", created.getTemplate());
        assertEquals("SELECT * FROM test", created.getSqlStr());
    }

    /**
     * Vérifie qu'en cas d'erreur de validation (ex : nom vide),
     * on reste sur la page "rule/add" sans redirection.
     */
    @Test
    //method POST /rule/validate → doit rester sur la page si la validation échoue (champ vide)
    void shouldReturnToAddFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/rule/validate")
                        .param("name", "") // champ invalide
                        .param("description", "Invalid test"))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/add"))
                .andExpect(model().hasErrors());
    }

    /**
     * Vérifie qu'une règle existante ne peut pas être recréée (cas de doublon).
     * Doit rester sur la page "rule/add" avec une erreur de validation.
     */
    @Test
    void shouldRejectDuplicateRuleCreation() throws Exception {
        // GIVEN — Une règle déjà existante en base
        Rule existingRule = new Rule();
        existingRule.setName("Duplicate Rule");
        existingRule.setDescription("Existing rule");
        ruleRepository.save(existingRule);

        // WHEN — On tente d'en créer une autre avec le même nom
        mockMvc.perform(post("/rule/validate")
                        .with(csrf())
                        .param("name", "Duplicate Rule") // même nom = doublon
                        .param("description", "Trying to duplicate"))
                .andExpect(status().isOk())                     // Pas de redirection
                .andExpect(view().name("rule/add"))             // Reste sur le formulaire
                .andExpect(model().hasErrors())                 // Erreurs de validation présentes
                .andExpect(model().attributeHasFieldErrors("rule", "name")); // Erreur spécifique sur le champ "name"

        // THEN — Vérifie qu'aucune règle supplémentaire n'a été ajoutée
        long count = ruleRepository.findAll().stream()
                .filter(r -> "Duplicate Rule".equals(r.getName()))
                .count();

        assertEquals(1, count, "Une seule règle doit exister avec ce nom");
    }

    // ============================================================
    // Méthode : showUpdateForm() → GET /rule/update/{id}
    // ============================================================

    /**
     * Vérifie que le formulaire de mise à jour s'affiche quand la règle demandée existe.
     */
    @Test
    void shouldShowUpdateFormWhenRuleExists() throws Exception {
        // GIVEN — on crée une règle existante en base
        Rule rule = new Rule();
        rule.setName("Existing Rule");
        rule.setDescription("Existing description");
        rule = ruleRepository.save(rule);

        // WHEN — on appelle la route /rule/update/{id}
        mockMvc.perform(get("/rule/update/" + rule.getRuleId()))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/update"))
                .andExpect(model().attributeExists("rule"))
                // Vérifie que le modèle contient bien la règle récupérée
                .andExpect(model().attribute("rule", org.hamcrest.Matchers.hasProperty("name", org.hamcrest.Matchers.is("Existing Rule"))));
    }

    /**
     * Vérifie que lorsqu'on cherche à modifier une règle inexistante,
     * on est redirigé vers "/rule/list".
     */
    @Test
    void shouldRedirectToListWhenRuleNotFound() throws Exception {
        // GIVEN — aucun enregistrement avec cet ID
        int nonExistentId = 9999;

        // WHEN — on tente d'accéder à la page update avec un ID inexistant
        mockMvc.perform(get("/rule/update/" + nonExistentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"));
    }

    // ============================================================
    // Méthode : updateRule() → POST /rule/update/{id}
    // ============================================================

    /**
     * Vérifie qu'en cas d'erreur de validation lors d'une mise à jour,
     * le contrôleur reste sur la page "rule/update".
     */
    @Test
    void shouldStayOnUpdatePageWhenValidationFails() throws Exception {
        // GIVEN — une règle existante
        Rule rule = new Rule();
        rule.setName("Initial Rule");
        rule.setDescription("Valid desc");
        rule = ruleRepository.save(rule);

        // WHEN — on tente une mise à jour avec un nom vide (invalide)
        mockMvc.perform(post("/rule/update/" + rule.getRuleId())
                        .with(csrf())
                        .param("name", "")  // champ invalide
                        .param("description", "Invalid update"))
                // THEN — reste sur la vue "rule/update"
                .andExpect(status().isOk())
                .andExpect(view().name("rule/update"))
                .andExpect(model().hasErrors());
    }

    /**
     * Vérifie qu'une règle existante peut être mise à jour avec succès.
     * Doit rediriger vers "/rule/list" et persister les changements.
     */
    @Test
    void shouldUpdateRuleSuccessfully() throws Exception {
        // GIVEN — Préparation : on insère une règle existante dans la base
        Rule rule = new Rule();
        rule.setName("Rule To Update");
        rule.setDescription("Old description");
        rule.setJson("{}");
        rule.setTemplate("Old Template");
        rule.setSqlStr("Old SQL");
        rule.setSqlPart("Old Part");
        rule = ruleRepository.save(rule);

        // WHEN — Envoi d'une requête POST simulant la mise à jour du formulaire
        mockMvc.perform(post("/rule/update/" + rule.getRuleId())
                        .with(csrf())
                        .param("name", "Updated Rule")
                        .param("description", "Updated description")
                        .param("json", "{\"a\":1}")
                        .param("template", "Updated Template")
                        .param("sqlStr", "Updated SQL")
                        .param("sqlPart", "Updated Part"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"));

        // THEN — Vérifie que la règle a bien été mise à jour en base
        Rule updated = ruleRepository.findById(rule.getRuleId())
                .orElseThrow(() -> new AssertionError("La règle mise à jour n'existe plus en base"));

        assertEquals("Updated Rule", updated.getName());
        assertEquals("Updated description", updated.getDescription());
        assertEquals("Updated Template", updated.getTemplate());
        assertEquals("Updated SQL", updated.getSqlStr());
    }

    /**
     * Vérifie que lorsqu'on tente de mettre à jour une règle inexistante,
     * le contrôleur redirige vers "/rule/list" avec un message d'erreur.
     */
    @Test
    void shouldRedirectToListWhenUpdatingNonExistingRule() throws Exception {
        // GIVEN — un identifiant inexistant
        int nonExistentId = 9999;

        // WHEN — on tente d'envoyer une mise à jour sur cet ID inexistant
        mockMvc.perform(post("/rule/update/" + nonExistentId)
                        .with(csrf())
                        .param("name", "Ghost Rule")
                        .param("description", "Should not exist"))
                // THEN — le contrôleur doit rediriger vers la liste
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"))
                .andExpect(flash().attributeExists("errorMessage"));

    }

    /**
     * Vérifie qu'une mise à jour refusée pour cause de doublon
     * reste sur la page "rule/update" avec une erreur de validation.
     */
    @Test
    void shouldRejectDuplicateRuleUpdate() throws Exception {
        // GIVEN — Deux règles existantes en base
        Rule ruleA = new Rule();
        ruleA.setName("Existing Rule");
        ruleA.setDescription("First one");
        ruleRepository.save(ruleA);

        Rule ruleB = new Rule();
        ruleB.setName("Other Rule");
        ruleB.setDescription("To be updated");
        ruleB = ruleRepository.save(ruleB);

        // WHEN — On tente de renommer ruleB avec le même nom que ruleA
        mockMvc.perform(post("/rule/update/" + ruleB.getRuleId())
                        .with(csrf())
                        .param("name", "Existing Rule") // Nom déjà pris
                        .param("description", "Trying duplicate name"))
                // THEN — Le contrôleur doit refuser la mise à jour
                .andExpect(status().isOk())                      // pas de redirection
                .andExpect(view().name("rule/update"))           // reste sur le formulaire
                .andExpect(model().hasErrors())                  // présence d'erreurs
                .andExpect(model().attributeHasFieldErrors("rule", "name")); // erreur sur le champ "name"

        // Vérifie que la règle B n'a pas été modifiée
        Rule unchanged = ruleRepository.findById(ruleB.getRuleId())
                .orElseThrow(() -> new AssertionError("La règle à mettre à jour n'existe plus"));
        assertEquals("Other Rule", unchanged.getName(), "Le nom ne doit pas avoir été modifié");
    }


// ============================================================
    // Méthode : deleteRule() → GET /rule/delete/{id}
    // ============================================================

    /**
     * Vérifie qu'une règle existante peut être supprimée avec succès.
     * Doit rediriger vers "/rule/list" et retirer la règle de la base.
     */
    @Test
    void shouldDeleteRuleSuccessfully() throws Exception {
        // GIVEN — Préparation : insertion d'une règle existante en base
        Rule rule = new Rule();
        rule.setName("Rule To Delete");
        rule.setDescription("Temporary rule");
        rule.setJson("{}");
        rule.setTemplate("Template to delete");
        rule.setSqlStr("DELETE FROM test");
        rule.setSqlPart("WHERE id=99");
        rule = ruleRepository.save(rule);

        Integer idToDelete = rule.getRuleId();
        assertTrue(ruleRepository.existsById(idToDelete), "La règle doit exister avant suppression");

        // WHEN — Requête GET simulant la suppression
        mockMvc.perform(get("/rule/delete/" + idToDelete)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"));

        // THEN — Vérification que la règle a bien été supprimée
        boolean existsAfterDelete = ruleRepository.existsById(idToDelete);
        assertFalse(existsAfterDelete, "La règle ne doit plus exister après suppression");
    }

    /**
     * Vérifie que lorsqu'on tente de supprimer une règle inexistante,
     * le contrôleur redirige vers "/rule/list" avec un message d'erreur flash.
     */
    @Test
    void shouldRedirectToListWhenDeletingNonExistingRule() throws Exception {
        // GIVEN — ID inexistant
        int nonExistentId = 9999;

        // WHEN — on appelle /rule/delete/{id} avec cet ID
        mockMvc.perform(get("/rule/delete/" + nonExistentId)
                        .with(csrf()))
                // THEN — redirection + message d'erreur
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

}

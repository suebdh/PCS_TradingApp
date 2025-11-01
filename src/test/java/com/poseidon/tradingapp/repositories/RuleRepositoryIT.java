package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration du RuleRepository.
 * <p>
 * Ce test valide la chaîne complète de persistance :
 * Repository Spring Data JPA → Hibernate → JDBC → Base MySQL (profil "test").
 * <p>
 * Il vérifie le bon fonctionnement des opérations CRUD (Create, Read, Update, Delete)
 * sur la table "rule" avec le vrai schéma de la base de test défini dans schema.sql.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RuleRepositoryIT {

	@Autowired
	private RuleRepository ruleRepository;

	@Test
	public void shouldPerformCrudOperations() {
		// Create
		Rule rule = new Rule();
		rule.setName("Rule Name");
		rule.setDescription("Description");
		rule.setJson("Json");
		rule.setTemplate("Template");
		rule.setSqlStr("SQL");
		rule.setSqlPart("SQL Part");

		rule = ruleRepository.save(rule);
		assertNotNull(rule.getRuleId());
        assertEquals("Rule Name", rule.getName());

		// Update
		rule.setName("Rule Name Update");
		rule = ruleRepository.save(rule);
        assertEquals("Rule Name Update", rule.getName());

		// Read
		List<Rule> listResult = ruleRepository.findAll();
        assertFalse(listResult.isEmpty());

		// Delete
		Integer id = rule.getRuleId();
		ruleRepository.delete(rule);
		Optional<Rule> ruleList = ruleRepository.findById(id);
		assertFalse(ruleList.isPresent());
	}
}

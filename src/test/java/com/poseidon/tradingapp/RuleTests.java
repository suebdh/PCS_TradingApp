package com.poseidon.tradingapp;

import com.poseidon.tradingapp.domain.Rule;
import com.poseidon.tradingapp.repositories.RuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RuleTests {

	@Autowired
	private RuleRepository ruleRepository;

	@Test
	public void ruleTest() {
		// Création de l'objet avec setters (constructeur vide + setters)
		Rule rule = new Rule();
		rule.setName("Rule Name");
		rule.setDescription("Description");
		rule.setJson("Json");
		rule.setTemplate("Template");
		rule.setSqlStr("SQL");
		rule.setSqlPart("SQL Part");

		// Save
		rule = ruleRepository.save(rule);
		assertNotNull(rule.getRuleId());
        assertEquals("Rule Name", rule.getName());

		// Update
		rule.setName("Rule Name Update");
		rule = ruleRepository.save(rule);
        assertEquals("Rule Name Update", rule.getName());

		// Find
		List<Rule> listResult = ruleRepository.findAll();
        assertFalse(listResult.isEmpty());

		// Delete
		Integer id = rule.getRuleId();
		ruleRepository.delete(rule);
		Optional<Rule> ruleList = ruleRepository.findById(id);
		assertFalse(ruleList.isPresent());
	}
}

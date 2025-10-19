package com.poseidon.tradingapp;

import com.poseidon.tradingapp.domain.RuleName;
import com.poseidon.tradingapp.repositories.RuleNameRepository;
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
	private RuleNameRepository ruleNameRepository;

	@Test
	public void ruleTest() {
		// Création de l'objet avec setters (constructeur vide + setters)
		RuleName rule = new RuleName();
		rule.setName("Rule Name");
		rule.setDescription("Description");
		rule.setJson("Json");
		rule.setTemplate("Template");
		rule.setSqlStr("SQL");
		rule.setSqlPart("SQL Part");

		// Save
		rule = ruleNameRepository.save(rule);
		assertNotNull(rule.getRuleNameId());
        assertEquals("Rule Name", rule.getName());

		// Update
		rule.setName("Rule Name Update");
		rule = ruleNameRepository.save(rule);
        assertEquals("Rule Name Update", rule.getName());

		// Find
		List<RuleName> listResult = ruleNameRepository.findAll();
        assertFalse(listResult.isEmpty());

		// Delete
		Integer id = rule.getRuleNameId();
		ruleNameRepository.delete(rule);
		Optional<RuleName> ruleList = ruleNameRepository.findById(id);
		assertFalse(ruleList.isPresent());
	}
}

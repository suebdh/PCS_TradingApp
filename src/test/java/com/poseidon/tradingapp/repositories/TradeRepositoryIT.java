package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.Trade;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

/**
 * Test d'intégration du TradeRepository.
 * <p>
 * Ce test valide la chaîne complète de persistance :
 * Repository Spring Data JPA → Hibernate → JDBC → Base MySQL (profil "test").
 * <p>
 * Il vérifie le bon fonctionnement des opérations CRUD (Create, Read, Update, Delete)
 * sur la table "trade" avec le vrai schéma de la base de test défini dans schema.sql.
 */
@ActiveProfiles("test")
@SpringBootTest
public class TradeRepositoryIT {

	@Autowired
	private TradeRepository tradeRepository;

	@BeforeEach
	void cleanDatabase(){
		tradeRepository.deleteAll();
	}

	@Test
	void shouldSaveTrade() {
		// given
		Trade trade = new Trade();
		trade.setAccount("Trade Account");
		trade.setType("Type");

		// when
		Trade saved = tradeRepository.save(trade);

		// then
		assertNotNull(saved.getTradeId());
		assertEquals("Trade Account", saved.getAccount());

	}

	@Test
	void shouldUpdateTrade() {
		// given
		Trade trade = new Trade();
		trade.setAccount("Initial Account");
		trade.setType("Type");
		trade = tradeRepository.save(trade);

		// when
		trade.setAccount("Updated Account");
		Trade updated = tradeRepository.save(trade);

		// then
		assertEquals("Updated Account", updated.getAccount());

	}

	@Test
	void shouldFindAllTrades() {
		// given
		Trade t1 = new Trade();
		t1.setAccount("Account1");
		t1.setType("Type1");
		tradeRepository.save(t1);

		Trade t2 = new Trade();
		t2.setAccount("Account2");
		t2.setType("Type2");
		tradeRepository.save(t2);

		// when
		List<Trade> trades = tradeRepository.findAll();

		// then
		assertEquals(2, trades.size());

	}

	@Test
	void shouldDeleteTrade() {
		// given
		Trade trade = new Trade();
		trade.setAccount("ToDelete");
		trade.setType("Type");
		Trade saved = tradeRepository.save(trade);
		Integer id = saved.getTradeId();

		// when
		tradeRepository.delete(saved);
		Optional<Trade> result = tradeRepository.findById(id);

		// then
		assertFalse(result.isPresent());

	}
}

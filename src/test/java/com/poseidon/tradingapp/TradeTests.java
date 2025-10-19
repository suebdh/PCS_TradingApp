package com.poseidon.tradingapp;

import com.poseidon.tradingapp.domain.Trade;
import com.poseidon.tradingapp.repositories.TradeRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeTests {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeTest() {
		// Création de l'objet avec setters (constructeur vide + setters)
		Trade trade = new Trade();
		trade.setAccount("Trade Account");
		trade.setType("Type");

		// Save
		trade = tradeRepository.save(trade);
		assertNotNull(trade.getTradeId());
		assertEquals("Trade Account", trade.getAccount()); //assertEquals(expected, actual);
		// Update
		trade.setAccount("Trade Account Update");
		trade = tradeRepository.save(trade);
        assertEquals("Trade Account Update", trade.getAccount());

		// Find
		List<Trade> listResult = tradeRepository.findAll();
        assertFalse(listResult.isEmpty());

		// Delete
		Integer id = trade.getTradeId();
		tradeRepository.delete(trade);
		Optional<Trade> tradeList = tradeRepository.findById(id);
		assertFalse(tradeList.isPresent());
	}
}

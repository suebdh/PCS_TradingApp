package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
//import org.junit.Assert; // JUnit 4
import static org.junit.jupiter.api.Assertions.*; // JUnit 5
//import org.junit.Test; // JUnit 4
import org.junit.jupiter.api.Test; // JUnit 5


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//import org.junit.runner.RunWith; // JUnit 4
//import org.springframework.test.context.junit4.SpringRunner; // JUnit 4
import org.junit.jupiter.api.extension.ExtendWith; // JUnit 5
import org.springframework.test.context.junit.jupiter.SpringExtension; // JUnit 5

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeTests {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeTest() {
		Trade trade = new Trade("Trade Account", "Type");

		// Save
		trade = tradeRepository.save(trade);
		assertNotNull(trade.getTradeId());
		assertTrue(trade.getAccount().equals("Trade Account"));

		// Update
		trade.setAccount("Trade Account Update");
		trade = tradeRepository.save(trade);
		assertTrue(trade.getAccount().equals("Trade Account Update"));

		// Find
		List<Trade> listResult = tradeRepository.findAll();
		assertTrue(listResult.size() > 0);

		// Delete
		Integer id = trade.getTradeId();
		tradeRepository.delete(trade);
		Optional<Trade> tradeList = tradeRepository.findById(id);
		assertFalse(tradeList.isPresent());
	}
}

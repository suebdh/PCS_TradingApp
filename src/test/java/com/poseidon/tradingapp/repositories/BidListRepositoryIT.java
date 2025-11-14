package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.BidList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration du BidListRepository.
 * <p>
 * Ce test valide la chaîne complète de persistance :
 * Repository Spring Data JPA → Hibernate → JDBC → Base MySQL (profil "test").
 * <p>
 * Il vérifie le bon fonctionnement des opérations CRUD (Create, Read, Update, Delete)
 * sur la table "Bidlist" avec le vrai schéma de la base de test défini dans schema.sql.
 */
@ActiveProfiles("test")
@SpringBootTest
public class BidListRepositoryIT {

	@Autowired
	private BidListRepository bidListRepository;

	@BeforeEach
	void cleanDatabase() {
		bidListRepository.deleteAll();
	}

	@Test
	void shouldSaveBidList() {
		// given
		BidList bid = new BidList();
		bid.setAccount("Account Test");
		bid.setType("Type Test");
		bid.setBidQuantity(10);

		// when
		BidList saved = bidListRepository.save(bid);

		// then
		assertNotNull(saved.getBidListId());
		assertEquals("Account Test", saved.getAccount());
		assertEquals(10, saved.getBidQuantity());
	}

	@Test
	void shouldUpdateBidList() {
		// given
		BidList bid = new BidList();
		bid.setAccount("Initial Account");
		bid.setType("Type Test");
		bid.setBidQuantity(10);
		bid = bidListRepository.save(bid);

		// when
		bid.setBidQuantity(20);
		BidList updated = bidListRepository.save(bid);

		// then
		assertEquals(20, updated.getBidQuantity());
	}

	@Test
	void shouldFindAllBidLists() {
		// given
		BidList b1 = new BidList();
		b1.setAccount("Account1");
		b1.setType("Type1");
		b1.setBidQuantity(10);
		bidListRepository.save(b1);

		BidList b2 = new BidList();
		b2.setAccount("Account2");
		b2.setType("Type2");
		b2.setBidQuantity(20);
		bidListRepository.save(b2);

		// when
		List<BidList> bids = bidListRepository.findAll();

		// then
		assertEquals(2, bids.size());
	}

	@Test
	void shouldDeleteBidList() {
		// given
		BidList bid = new BidList();
		bid.setAccount("ToDelete");
		bid.setType("Type Test");
		bid.setBidQuantity(10);
		BidList saved = bidListRepository.save(bid);
		Integer id = saved.getBidListId();

		// when
		bidListRepository.delete(saved);
		Optional<BidList> result = bidListRepository.findById(id);

		// then
		assertFalse(result.isPresent());
	}
}

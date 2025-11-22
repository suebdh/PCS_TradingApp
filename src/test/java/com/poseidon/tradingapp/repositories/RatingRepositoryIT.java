package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class RatingRepositoryIT {

	@Autowired
	private RatingRepository ratingRepository;

	@BeforeEach
	void cleanDatabase() {
		ratingRepository.deleteAll();
	}

	@Test
	void shouldSaveRating() {
		// given
		Rating rating = new Rating();
		rating.setMoodysRating("Moodys Rating");
		rating.setSandPRating("Sand PRating");
		rating.setFitchRating("Fitch Rating");
		rating.setOrderNumber(10);

		// when
		Rating saved = ratingRepository.save(rating);

		// then
		assertNotNull(saved.getRatingId());
		assertEquals(10, saved.getOrderNumber());
	}

	@Test
	void shouldUpdateRating() {
		// given
		Rating rating = new Rating();
		rating.setMoodysRating("Moodys Rating");
		rating.setSandPRating("Sand PRating");
		rating.setFitchRating("Fitch Rating");
		rating.setOrderNumber(10);
		Rating saved = ratingRepository.save(rating);

		// when
		saved.setOrderNumber(20);
		Rating updated = ratingRepository.save(saved);

		// then
		assertNotNull(updated.getRatingId());
		assertEquals(20, (int) updated.getOrderNumber());
	}

	@Test
	void shouldFindRating() {
		// given
		Rating rating = new Rating();
		rating.setMoodysRating("Moodys Rating");
		rating.setSandPRating("Sand PRating");
		rating.setFitchRating("Fitch Rating");
		rating.setOrderNumber(10);
		Rating saved = ratingRepository.save(rating);

		// when
		List<Rating> listResult = ratingRepository.findAll();

		// then
		assertFalse(listResult.isEmpty());
		assertEquals(1, listResult.size());
		assertEquals(saved.getRatingId(), listResult.get(0).getRatingId());
	}

	@Test
	void shouldDeleteRating() {
		// given
		Rating rating = new Rating();
		rating.setMoodysRating("Moodys Rating");
		rating.setSandPRating("Sand PRating");
		rating.setFitchRating("Fitch Rating");
		rating.setOrderNumber(10);
		Rating saved = ratingRepository.save(rating);
		Integer id = saved.getRatingId();

		// when
		ratingRepository.delete(saved);

		// then
		Optional<Rating> result = ratingRepository.findById(id);
		assertFalse(result.isPresent());
	}
}

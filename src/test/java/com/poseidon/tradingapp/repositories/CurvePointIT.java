package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.CurvePoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration du CurvePointRepository.
 *
 * Vérifie la chaîne complète :
 * Repository Spring Data JPA → Hibernate → JDBC → Base MySQL (profil "test").
 *
 * Couvre toutes les opérations CRUD sur la table "CurvePoint".
 */
@ActiveProfiles("test")
@SpringBootTest
public class CurvePointIT {

	@Autowired
	private CurvePointRepository curvePointRepository;

	@BeforeEach
	void cleanDatabase() {
		curvePointRepository.deleteAll();
	}

	@Test
	void shouldSaveCurvePoint() {
		// given
		CurvePoint cp = new CurvePoint();
		cp.setCurveId(10);
		cp.setTerm(new BigDecimal("10.0000"));
		cp.setValue(new BigDecimal("30.0000"));

		// when
		CurvePoint saved = curvePointRepository.save(cp);

		// then
		assertNotNull(saved.getCurvePointId());
		assertEquals(10, saved.getCurveId());
		assertEquals(new BigDecimal("10.0000"), saved.getTerm());
		assertEquals(new BigDecimal("30.0000"), saved.getValue());
	}

	@Test
	void shouldUpdateCurvePoint() {
		// given
		CurvePoint cp = new CurvePoint();
		cp.setCurveId(10);
		cp.setTerm(new BigDecimal("1.0000"));
		cp.setValue(new BigDecimal("2.0000"));
		cp = curvePointRepository.save(cp);

		// when
		cp.setCurveId(20);
		cp.setTerm(new BigDecimal("5.5000"));
		cp.setValue(new BigDecimal("9.9000"));
		CurvePoint updated = curvePointRepository.save(cp);

		// then
		assertEquals(20, updated.getCurveId());
		assertEquals(new BigDecimal("5.5000"), updated.getTerm());
		assertEquals(new BigDecimal("9.9000"), updated.getValue());
	}

	@Test
	void shouldFindAllCurvePoints() {
		// given
		CurvePoint c1 = new CurvePoint();
		c1.setCurveId(1);
		c1.setTerm(new BigDecimal("1.0000"));
		c1.setValue(new BigDecimal("2.0000"));
		curvePointRepository.save(c1);

		CurvePoint c2 = new CurvePoint();
		c2.setCurveId(2);
		c2.setTerm(new BigDecimal("3.0000"));
		c2.setValue(new BigDecimal("4.0000"));
		curvePointRepository.save(c2);

		// when
		List<CurvePoint> points = curvePointRepository.findAll();

		// then
		assertEquals(2, points.size());
	}

	@Test
	void shouldDeleteCurvePoint() {
		// given
		CurvePoint cp = new CurvePoint();
		cp.setCurveId(10);
		cp.setTerm(new BigDecimal("10.0000"));
		cp.setValue(new BigDecimal("30.0000"));
		CurvePoint saved = curvePointRepository.save(cp);
		Integer id = saved.getCurvePointId();

		// when
		curvePointRepository.delete(saved);
		Optional<CurvePoint> result = curvePointRepository.findById(id);

		// then
		assertFalse(result.isPresent());
	}

}

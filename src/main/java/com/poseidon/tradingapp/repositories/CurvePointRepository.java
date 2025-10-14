package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

}

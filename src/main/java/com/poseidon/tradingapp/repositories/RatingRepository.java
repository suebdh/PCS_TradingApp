package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

}

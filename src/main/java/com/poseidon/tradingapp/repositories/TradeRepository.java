package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeRepository extends JpaRepository<Trade, Integer> {
}

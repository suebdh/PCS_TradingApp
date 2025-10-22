package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Integer> {
}

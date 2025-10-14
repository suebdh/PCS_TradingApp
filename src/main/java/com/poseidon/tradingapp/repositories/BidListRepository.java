package com.poseidon.tradingapp.repositories;

import com.poseidon.tradingapp.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BidListRepository extends JpaRepository<BidList, Integer> {

}

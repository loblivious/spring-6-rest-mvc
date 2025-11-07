package com.loblivious.spring6restmvc.repositories;

import com.loblivious.spring6restmvc.entities.BeerOrder;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {

}

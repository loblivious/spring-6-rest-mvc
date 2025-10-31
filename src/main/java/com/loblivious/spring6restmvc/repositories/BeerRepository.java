package com.loblivious.spring6restmvc.repositories;

import com.loblivious.spring6restmvc.entities.Beer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

}

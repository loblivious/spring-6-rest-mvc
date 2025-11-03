package com.loblivious.spring6restmvc.repositories;

import com.loblivious.spring6restmvc.entities.Beer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID>, QuerydslPredicateExecutor<Beer> {

}

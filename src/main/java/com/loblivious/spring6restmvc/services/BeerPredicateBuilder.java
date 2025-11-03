package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.BeerFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;
import static com.loblivious.spring6restmvc.entities.QBeer.beer;

public class BeerPredicateBuilder {
  public static Predicate build(BeerFilterDTO beerFilter) {
    BooleanBuilder builder = new BooleanBuilder();

    if (StringUtils.hasText(beerFilter.beerName())) {
      builder.and(beer.beerName.containsIgnoreCase(beerFilter.beerName()));
    }

    if (beerFilter.beerStyle() != null) {
      builder.and(beer.beerStyle.eq(beerFilter.beerStyle()));
    }

    return builder;
  }
}

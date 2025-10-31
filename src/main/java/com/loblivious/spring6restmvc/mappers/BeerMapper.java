package com.loblivious.spring6restmvc.mappers;

import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
  Beer beerDtoToBeer(BeerDTO beerDto);

  BeerDTO beerToBeerDto(Beer beer);
}

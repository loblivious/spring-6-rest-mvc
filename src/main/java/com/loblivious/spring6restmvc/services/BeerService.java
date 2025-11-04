package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.model.BeerFilterDTO;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeerService {

  Page<BeerDTO> listBeers(BeerFilterDTO beerFilterDTO, Pageable pageable);

  BeerDTO getBeerById(UUID id);

  BeerDTO saveNewBeer(BeerDTO beer);

  void updateBeerById(UUID beerId, BeerDTO beer);

  void deleteBeerById(UUID beerId);

  void patchBeerById(UUID beerId, BeerDTO beer);
}

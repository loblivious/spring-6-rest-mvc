package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.model.BeerFilterDTO;
import java.util.List;
import java.util.UUID;

public interface BeerService {

  List<BeerDTO> listBeers(BeerFilterDTO beerFilterDTO);

  BeerDTO getBeerById(UUID id);

  BeerDTO saveNewBeer(BeerDTO beer);

  void updateBeerById(UUID beerId, BeerDTO beer);

  void deleteBeerById(UUID beerId);

  void patchBeerById(UUID beerId, BeerDTO beer);
}

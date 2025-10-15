package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.Beer;
import java.util.List;
import java.util.UUID;

public interface BeerService {

  List<Beer> listBeers();

  Beer getBeerById(UUID id);
}

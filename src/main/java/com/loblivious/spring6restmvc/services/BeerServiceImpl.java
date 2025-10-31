package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.mappers.BeerMapper;
import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.repositories.BeerRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  @Override
  public List<BeerDTO> listBeers() {
    return beerRepository.findAll().stream().map(beerMapper::beerToBeerDto)
        .collect(Collectors.toList());
  }

  @Override
  public BeerDTO getBeerById(UUID id) {
    return beerRepository.findById(id)
        .map(beerMapper::beerToBeerDto)
        .orElseThrow(NotFoundException::new);
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beer) {
    return null;
  }

  @Override
  public void updateBeerById(UUID beerId, BeerDTO beer) {

  }

  @Override
  public void deleteBeerById(UUID beerId) {

  }

  @Override
  public void patchBeerById(UUID beerId, BeerDTO beer) {

  }
}

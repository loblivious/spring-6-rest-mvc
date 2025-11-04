package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.mappers.BeerMapper;
import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.model.BeerFilterDTO;
import com.loblivious.spring6restmvc.repositories.BeerRepository;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  @Override
  public List<BeerDTO> listBeers(BeerFilterDTO beerFilter) {
    Predicate predicate = BeerPredicateBuilder.build(beerFilter);

    Iterable<Beer> beers = beerRepository.findAll(predicate);

    return StreamSupport.stream(beers.spliterator(), false)
        .map((beer) -> {
          BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
          if (beerFilter.showInventory() != null && !beerFilter.showInventory()) {
            beerDto.setQuantityOnHand(null);
          }
          return beerDto;
        }).toList();
  }

  @Override
  public BeerDTO getBeerById(UUID id) {
    return beerRepository.findById(id)
        .map(beerMapper::beerToBeerDto)
        .orElseThrow(NotFoundException::new);
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beerDto) {
    return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
  }

  @Override
  public void updateBeerById(UUID beerId, BeerDTO beerDto) {
    Beer foundBeer = beerRepository.findById(beerId)
        .orElseThrow(NotFoundException::new);

    foundBeer.setBeerName(beerDto.getBeerName());
    foundBeer.setBeerStyle(beerDto.getBeerStyle());
    foundBeer.setUpc(beerDto.getUpc());
    foundBeer.setPrice(beerDto.getPrice());
    foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());

    beerRepository.save(foundBeer);
  }

  @Override
  public void deleteBeerById(UUID beerId) {
    if (!beerRepository.existsById(beerId)) {
      throw new NotFoundException();
    }
    beerRepository.deleteById(beerId);
  }

  @Override
  public void patchBeerById(UUID beerId, BeerDTO beer) {
    Beer foundBeer = beerRepository.findById(beerId)
        .orElseThrow(NotFoundException::new);

    if (StringUtils.hasText(beer.getBeerName())) {
      foundBeer.setBeerName(beer.getBeerName());
    }
    if (beer.getBeerStyle() != null) {
      foundBeer.setBeerStyle(beer.getBeerStyle());
    }
    if (StringUtils.hasText(beer.getUpc())) {
      foundBeer.setUpc(beer.getUpc());
    }
    if (beer.getPrice() != null) {
      foundBeer.setPrice(beer.getPrice());
    }
    if (beer.getQuantityOnHand() != null) {
      foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
    }

    beerRepository.save(foundBeer);
  }
}

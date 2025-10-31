package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.model.BeerStyle;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class BeerServiceWithoutDbImpl implements BeerService {

  private final Map<UUID, BeerDTO> beerMap;

  public BeerServiceWithoutDbImpl() {
    this.beerMap = new HashMap<>();

    BeerDTO beer1 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Galaxy Cat")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12356")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    BeerDTO beer2 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Crank")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12356222")
        .price(new BigDecimal("11.99"))
        .quantityOnHand(392)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    BeerDTO beer3 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Sunshine City")
        .beerStyle(BeerStyle.IPA)
        .upc("12356")
        .price(new BigDecimal("13.99"))
        .quantityOnHand(144)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    beerMap.put(beer1.getId(), beer1);
    beerMap.put(beer2.getId(), beer2);
    beerMap.put(beer3.getId(), beer3);
  }

  @Override
  public List<BeerDTO> listBeers() {
    return new ArrayList<>(beerMap.values());
  }

  @Override
  public BeerDTO getBeerById(UUID id) {
    log.debug("Get Beer by Id - in service. Id: {}", id.toString());

    return Optional.ofNullable(beerMap.get(id)).orElseThrow(NotFoundException::new);
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beer) {
    BeerDTO savedBeer = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .beerName(beer.getBeerName())
        .beerStyle(beer.getBeerStyle())
        .quantityOnHand(beer.getQuantityOnHand())
        .upc(beer.getUpc())
        .price(beer.getPrice())
        .build();

    beerMap.put(savedBeer.getId(), savedBeer);
    return savedBeer;
  }

  @Override
  public void updateBeerById(UUID beerId, BeerDTO beer) {
    BeerDTO existingBeer = beerMap.get(beerId);
    existingBeer.setBeerName(beer.getBeerName());
    existingBeer.setBeerStyle(beer.getBeerStyle());
    existingBeer.setPrice(beer.getPrice());
    existingBeer.setUpc(beer.getUpc());
    existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
  }

  @Override
  public void deleteBeerById(UUID beerId) {
    beerMap.remove(beerId);
  }

  @Override
  public void patchBeerById(UUID beerId, BeerDTO beer) {
    BeerDTO existingBeer = beerMap.get(beerId);

    if (existingBeer == null) {
      return;
    }

    if (StringUtils.hasText(beer.getBeerName())) {
      existingBeer.setBeerName(beer.getBeerName());
    }

    if (beer.getBeerStyle() != null) {
      existingBeer.setBeerStyle(beer.getBeerStyle());
    }

    if (beer.getPrice() != null) {
      existingBeer.setPrice(beer.getPrice());
    }

    if (beer.getQuantityOnHand() != null) {
      existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
    }

    if (StringUtils.hasText(beer.getUpc())) {
      existingBeer.setUpc(beer.getUpc());
    }
  }
}

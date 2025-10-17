package com.loblivious.spring6restmvc.controller;

import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.services.BeerService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

  public static final String BEER_PATH = "/api/v1/beer";
  public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

  private final BeerService beerService;

  @PatchMapping(BEER_PATH_ID)
  public ResponseEntity<Void> patchBeerById(@PathVariable("beerId") UUID beerId,
      @RequestBody BeerDTO beer) {
    log.info("Patching beer with id {}", beerId);

    beerService.patchBeerById(beerId, beer);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(BEER_PATH_ID)
  public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") UUID beerId) {
    log.info("Deleting Beer with id {}", beerId);

    beerService.deleteBeerById(beerId);

    return ResponseEntity.noContent().build();
  }

  @PutMapping(BEER_PATH_ID)
  public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") UUID beerId,
      @RequestBody BeerDTO beer) {
    log.info("Received Beer put request: beerId={}, beer={}", beerId, beer);

    beerService.updateBeerById(beerId, beer);

    return ResponseEntity.noContent().build();
  }

  @PostMapping(BEER_PATH)
  public ResponseEntity<Void> createBeer(@RequestBody BeerDTO beer) {
    log.info("Received Beer post request: {}", beer);

    BeerDTO savedBeer = beerService.saveNewBeer(beer);

    return ResponseEntity.created(URI.create(BEER_PATH + savedBeer.getId())).build();
  }

  @GetMapping(BEER_PATH)
  public List<BeerDTO> listBeers() {
    return beerService.listBeers();
  }

  @GetMapping(BEER_PATH_ID)
  public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
    log.info("Get Beer by Id - in controller");

    return beerService.getBeerById(beerId);
  }
}

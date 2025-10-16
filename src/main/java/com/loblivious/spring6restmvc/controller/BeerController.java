package com.loblivious.spring6restmvc.controller;

import com.loblivious.spring6restmvc.model.Beer;
import com.loblivious.spring6restmvc.services.BeerService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

  private final BeerService beerService;

  @PutMapping("/{beerId}")
  public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") UUID beerId,
      @RequestBody Beer beer) {
    log.info("Received Beer put request: beerId={}, beer={}", beerId, beer);

    beerService.updateBeerById(beerId, beer);

    return ResponseEntity.noContent()
        .build();
  }

  @PostMapping
  public ResponseEntity<Void> createBeer(@RequestBody Beer beer) {
    log.info("Received Beer post request: {}", beer);

    Beer savedBeer = beerService.saveNewBeer(beer);

    return ResponseEntity.created(URI.create("/api/v1/beer/" + savedBeer.getId()))
        .build();
  }

  @GetMapping
  public List<Beer> listBeers() {
    return beerService.listBeers();
  }

  @GetMapping(value = "/{beerId}")
  public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
    log.info("Get Beer by Id - in controller");

    return beerService.getBeerById(beerId);
  }
}

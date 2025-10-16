package com.loblivious.spring6restmvc.controller;

import com.loblivious.spring6restmvc.model.Beer;
import com.loblivious.spring6restmvc.services.BeerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

  private final BeerService beerService;

  @PostMapping
  public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
    log.info("Received Beer post request: {}", beer);

    Beer savedBeer = beerService.saveNewBeer(beer);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/api/v1/beer/" + savedBeer.getId()
        .toString());

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @GetMapping
  public List<Beer> listBeers() {
    return beerService.listBeers();
  }

  @GetMapping(value = "/{beerId}")
  public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
    log.debug("Get Beer by Id - in controller");

    return beerService.getBeerById(beerId);
  }
}

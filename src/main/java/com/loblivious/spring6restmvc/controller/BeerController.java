package com.loblivious.spring6restmvc.controller;

import com.loblivious.spring6restmvc.model.Beer;
import com.loblivious.spring6restmvc.services.BeerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

  private final BeerService beerService;

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

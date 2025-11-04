package com.loblivious.spring6restmvc.controller;

import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.model.BeerFilterDTO;
import com.loblivious.spring6restmvc.model.BeerStyle;
import com.loblivious.spring6restmvc.services.BeerService;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

  public static final String BEER_PATH = "/api/v1/beer";
  public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

  private static final String DEFAULT_SORTING_COLUMN_AND_DIRECTION = "beerName, asc";
  private static final int DEFAULT_PAGE_SIZE = 25;

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
      @Validated @RequestBody BeerDTO beer) {
    log.info("Received Beer put request: beerId={}, beer={}", beerId, beer);

    beerService.updateBeerById(beerId, beer);

    return ResponseEntity.noContent().build();
  }

  @PostMapping(BEER_PATH)
  public ResponseEntity<Void> createBeer(@Validated @RequestBody BeerDTO beer) {
    log.info("Received Beer post request: {}", beer);

    BeerDTO savedBeer = beerService.saveNewBeer(beer);

    return ResponseEntity.created(URI.create(BEER_PATH + "/" + savedBeer.getId())).build();
  }

  @GetMapping(BEER_PATH)
  public Page<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
      @RequestParam(required = false) BeerStyle beerStyle,
      @RequestParam(required = false) Boolean showInventory,
      @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORTING_COLUMN_AND_DIRECTION) Pageable pageable) {

    BeerFilterDTO beerFilterDto = BeerFilterDTO.builder()
        .beerName(beerName)
        .beerStyle(beerStyle)
        .showInventory(showInventory)
        .build();

    return beerService.listBeers(beerFilterDto, pageable);
  }

  @GetMapping(BEER_PATH_ID)
  public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
    log.info("Get Beer by Id - in controller");

    return beerService.getBeerById(beerId);
  }
}

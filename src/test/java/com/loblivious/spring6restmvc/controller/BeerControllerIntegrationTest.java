package com.loblivious.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.mappers.BeerMapper;
import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.repositories.BeerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerControllerIntegrationTest {

  @Autowired
  private BeerController beerController;

  @Autowired
  private BeerRepository beerRepository;

  @Autowired
  private BeerMapper beerMapper;

  @Test
  void testListBeers() {
    List<BeerDTO> dtos = beerController.listBeers();

    assertThat(dtos.size()).isEqualTo(3);
  }

  @Test
  // if anything fails, rollback to the original state
  @Transactional
  // rollback after the test
  @Rollback
  void testEmptyList() {
    beerRepository.deleteAll();
    List<BeerDTO> dtos = beerController.listBeers();

    assertThat(dtos.size()).isEqualTo(0);
  }

  @Test
  void testGetById() {
    Beer beer = beerRepository.findAll().getFirst();

    BeerDTO beerDto = beerController.getBeerById(beer.getId());

    assertThat(beerDto).isNotNull();
  }

  @Test
  void testGetByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> beerController.getBeerById(UUID.randomUUID()));
  }

  @Test
  @Transactional
  @Rollback
  void testSaveNewBeer() {
    BeerDTO beerDto = BeerDTO.builder().beerName("New Beer").build();

    ResponseEntity<Void> responseEntity = beerController.createBeer(beerDto);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID savedUUID = UUID.fromString(locationUUID[4]);

    assertThat(beerRepository.findById(savedUUID)).isPresent();
  }

  @Test
  @Transactional
  @Rollback
  void testUpdateBeerById() {
    Beer beer = beerRepository.findAll().getFirst();
    BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
    beerDto.setId(null);
    beerDto.setVersion(null);
    final String beerName = "UPDATED";
    beerDto.setBeerName(beerName);

    ResponseEntity<Void> responseEntity = beerController.updateBeerById(beer.getId(), beerDto);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Beer updatedBeer = beerRepository.findById(beer.getId()).orElse(null);
    assertThat(updatedBeer).isNotNull();
    assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
  }

  @Test
  void testUpdateBeerByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> beerController.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build()));
  }

  @Test
  @Transactional
  @Rollback
  void testDeleteById() {
    UUID beerId = beerRepository.findAll().getFirst().getId();

    ResponseEntity<Void> responseEntity = beerController.deleteBeerById(beerId);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    assertThat(beerRepository.findById(beerId)).isNotPresent();

  }

  @Test
  void testDeleteByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> beerController.deleteBeerById(UUID.randomUUID()));
  }
}
package com.loblivious.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.repositories.BeerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerControllerIntegrationTest {

  @Autowired
  private BeerController beerController;

  @Autowired
  private BeerRepository beerRepository;

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
}
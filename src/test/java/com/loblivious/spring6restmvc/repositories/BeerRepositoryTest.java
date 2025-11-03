package com.loblivious.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loblivious.spring6restmvc.bootstrap.BootstrapData;
import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.model.BeerFilterDTO;
import com.loblivious.spring6restmvc.model.BeerStyle;
import com.loblivious.spring6restmvc.services.BeerCsvServiceImpl;
import com.loblivious.spring6restmvc.services.BeerPredicateBuilder;
import jakarta.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

  @Autowired
  private BeerRepository beerRepository;

  @Test
  void testGetBeerListByName() {
    List<Beer> beerList = (List<Beer>) beerRepository.findAll(
        BeerPredicateBuilder.build(BeerFilterDTO.builder().beerName("IPA").build()));

    assertThat(beerList.size()).isEqualTo(336);
  }

  @Test
  void testSaveBeerNameTooLong() {
    assertThrows(ConstraintViolationException.class, () -> {
      beerRepository.save(Beer.builder()
          .beerName("My Beer 01234567890123456789012345678901234567890123456789")
          .beerStyle(BeerStyle.PALE_ALE)
          .upc("12345")
          .price(new BigDecimal("11.99"))
          .build());

      beerRepository.flush();
    });
  }

  @Test
  void testSaveBeer() {
    Beer savedBeer = beerRepository.save(Beer.builder()
        .beerName("My Beer")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12345")
        .price(new BigDecimal("11.99"))
        .build());

    // need to explicitly flush the repository in order to see the validation constraints kick in
    beerRepository.flush();

    assertThat(savedBeer).isNotNull();
    assertThat(savedBeer.getId()).isNotNull();
  }
}
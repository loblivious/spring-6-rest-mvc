package com.loblivious.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BeerRepositoryTest {

  @Autowired
  private BeerRepository beerRepository;

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
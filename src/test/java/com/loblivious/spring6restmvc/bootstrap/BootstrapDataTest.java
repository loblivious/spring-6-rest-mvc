package com.loblivious.spring6restmvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import com.loblivious.spring6restmvc.repositories.BeerRepository;
import com.loblivious.spring6restmvc.repositories.CustomerRepository;
import com.loblivious.spring6restmvc.services.BeerCsvService;
import com.loblivious.spring6restmvc.services.BeerCsvServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  BeerCsvService beerCsvService;

  BootstrapData bootstrapData;

  @BeforeEach
  void setUp() {
    bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
  }

  @Test
  @SneakyThrows
  void testRun() {
    bootstrapData.run();

    assertThat(beerRepository.count()).isEqualTo(2413);
    assertThat(customerRepository.count()).isEqualTo(3);
  }
}
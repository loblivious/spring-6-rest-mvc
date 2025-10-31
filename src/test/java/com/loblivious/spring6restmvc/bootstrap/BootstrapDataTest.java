package com.loblivious.spring6restmvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import com.loblivious.spring6restmvc.repositories.BeerRepository;
import com.loblivious.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BootstrapDataTest {

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  CustomerRepository customerRepository;

  BootstrapData bootstrapData;

  @BeforeEach
  void setUp() {
    bootstrapData = new BootstrapData(beerRepository, customerRepository);
  }

  @Test
  void testRun() {
    bootstrapData.run();

    assertThat(beerRepository.count()).isEqualTo(3);
    assertThat(customerRepository.count()).isEqualTo(3);
  }
}
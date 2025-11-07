package com.loblivious.spring6restmvc.repositories;

import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.entities.BeerOrder;
import com.loblivious.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerOrderRepositoryTest {

  @Autowired
  BeerOrderRepository beerOrderRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  BeerRepository beerRepository;

  Customer testCustomer;
  Beer testBeer;

  @BeforeEach
  void setUp() {
    testCustomer = customerRepository.findAll().getFirst();
    testBeer = beerRepository.findAll().getFirst();
  }

  @Transactional
  @Test
  void testBeerOrders() {
    BeerOrder beerOrder = BeerOrder.builder()
        .customerRef("Test Order")
        .customer(testCustomer)
        .build();

    BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

    System.out.println(savedBeerOrder.getCustomerRef());
  }
}
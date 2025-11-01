package com.loblivious.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.loblivious.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CustomerRepositoryTest {

  @Autowired
  private CustomerRepository customerRepository;

  @Test
  void saveCustomer() {
    Customer savedCustomer = customerRepository.save(Customer.builder().name("New Name").build());

    customerRepository.flush();

    assertThat(savedCustomer).isNotNull();
    assertThat(savedCustomer.getId()).isNotNull();
  }
}
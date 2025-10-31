package com.loblivious.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loblivious.spring6restmvc.entities.Customer;
import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.model.CustomerDTO;
import com.loblivious.spring6restmvc.repositories.CustomerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CustomerControllerIntegrationTest {

  @Autowired
  private CustomerController customerController;

  @Autowired
  private CustomerRepository customerRepository;

  @Test
  void testListCustomers() {
    List<CustomerDTO> dtos = customerController.listCustomers();

    assertThat(dtos.size()).isEqualTo(3);
  }

  @Test
  // if anything fails, rollback to the original state
  @Transactional
  // rollback after the test
  @Rollback
  void testEmptyList() {
    customerRepository.deleteAll();
    List<CustomerDTO> dtos = customerController.listCustomers();

    assertThat(dtos.size()).isEqualTo(0);
  }

  @Test
  void testGetById() {
    Customer customer = customerRepository.findAll().getFirst();

    CustomerDTO customerDto = customerController.getCustomerById(customer.getId());

    assertThat(customerDto).isNotNull();
  }

  @Test
  void testGetByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> customerController.getCustomerById(UUID.randomUUID()));
  }
}
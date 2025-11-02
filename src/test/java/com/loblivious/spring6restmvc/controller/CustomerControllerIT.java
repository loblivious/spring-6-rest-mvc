package com.loblivious.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loblivious.spring6restmvc.entities.Customer;
import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.mappers.CustomerMapper;
import com.loblivious.spring6restmvc.model.CustomerDTO;
import com.loblivious.spring6restmvc.repositories.CustomerRepository;
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
class CustomerControllerIT {

  @Autowired
  private CustomerController customerController;

  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private CustomerMapper customerMapper;

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

  @Test
  @Transactional
  @Rollback
  void testSaveNewCustomer() {
    CustomerDTO customerDto = CustomerDTO.builder().name("New Customer").build();

    ResponseEntity<Void> responseEntity = customerController.createCustomer(customerDto);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID savedUUID = UUID.fromString(locationUUID[4]);

    assertThat(customerRepository.findById(savedUUID)).isPresent();
  }

  @Test
  @Transactional
  @Rollback
  void testUpdateCustomerById() {
    Customer customer = customerRepository.findAll().getFirst();
    CustomerDTO customerDto = customerMapper.customerToCustomerDto(customer);
    customerDto.setId(null);
    customerDto.setVersion(null);
    final String customerName = "UPDATED";
    customerDto.setName(customerName);

    ResponseEntity<Void> responseEntity = customerController.updateCustomerById(customer.getId(),
        customerDto);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Customer updatedCustomer = customerRepository.findById(customer.getId()).orElse(null);
    assertThat(updatedCustomer).isNotNull();
    assertThat(updatedCustomer.getName()).isEqualTo(customerName);
  }

  @Test
  void testUpdateCustomerByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder()
            .build()));
  }

  @Test
  @Transactional
  @Rollback
  void testDeleteById() {
    UUID customerId = customerRepository.findAll().getFirst().getId();

    ResponseEntity<Void> responseEntity = customerController.deleteCustomerById(customerId);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    assertThat(customerRepository.findById(customerId)).isNotPresent();
  }

  @Test
  void testDeleteByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> customerController.deleteCustomerById(UUID.randomUUID()));
  }
}
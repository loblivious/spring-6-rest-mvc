package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.mappers.CustomerMapper;
import com.loblivious.spring6restmvc.model.CustomerDTO;
import com.loblivious.spring6restmvc.repositories.CustomerRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  public CustomerDTO getCustomerById(UUID id) {
    return customerRepository.findById(id)
        .map(customerMapper::customerToCustomerDto)
        .orElseThrow(NotFoundException::new);
  }

  @Override
  public List<CustomerDTO> getAllCustomers() {
    return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto)
        .collect(Collectors.toList());
  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {
    return null;
  }

  @Override
  public void updateCustomerById(UUID customerId, CustomerDTO customer) {

  }

  @Override
  public void deleteCustomerById(UUID customerId) {

  }

  @Override
  public void patchCustomerById(UUID customerId, CustomerDTO customer) {

  }
}

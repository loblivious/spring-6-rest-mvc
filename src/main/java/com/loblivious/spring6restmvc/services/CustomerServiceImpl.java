package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.entities.Customer;
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
import org.springframework.util.StringUtils;

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
  public CustomerDTO saveNewCustomer(CustomerDTO customerDto) {
    return customerMapper.customerToCustomerDto(
        customerRepository.save(customerMapper.custmerDtoToCustomer(customerDto)));
  }

  @Override
  public void updateCustomerById(UUID customerId, CustomerDTO customerDto) {
    Customer foundCustomer = customerRepository.findById(customerId)
        .orElseThrow(NotFoundException::new);

    foundCustomer.setName(customerDto.getName());

    customerRepository.save(foundCustomer);
  }

  @Override
  public void deleteCustomerById(UUID customerId) {
    if (!customerRepository.existsById(customerId)) {
      throw new NotFoundException();
    }
    customerRepository.deleteById(customerId);
  }

  @Override
  public void patchCustomerById(UUID customerId, CustomerDTO customer) {
    Customer foundCustomer = customerRepository.findById(customerId)
        .orElseThrow(NotFoundException::new);

    if (StringUtils.hasText(customer.getName())) {
      foundCustomer.setName(customer.getName());
    }

    customerRepository.save(foundCustomer);
  }
}

package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.CustomerDTO;
import java.util.List;
import java.util.UUID;

public interface CustomerService {

  CustomerDTO getCustomerById(UUID uuid);

  List<CustomerDTO> getAllCustomers();

  CustomerDTO saveNewCustomer(CustomerDTO customer);

  void updateCustomerById(UUID customerId, CustomerDTO customer);

  void deleteCustomerById(UUID customerId);

  void patchCustomerById(UUID customerId, CustomerDTO customer);
}

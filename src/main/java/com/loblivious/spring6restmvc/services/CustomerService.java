package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.Customer;
import java.util.List;
import java.util.UUID;

public interface CustomerService {

  Customer getCustomerById(UUID uuid);

  List<Customer> getAllCustomers();
}

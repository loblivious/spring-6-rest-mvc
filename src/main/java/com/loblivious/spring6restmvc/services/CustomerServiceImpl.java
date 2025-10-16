package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.Customer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

  private final Map<UUID, Customer> customerMap;

  public CustomerServiceImpl() {
    Customer customer1 = Customer.builder()
        .id(UUID.randomUUID())
        .name("Customer 1")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Customer customer2 = Customer.builder()
        .id(UUID.randomUUID())
        .name("Customer 2")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Customer customer3 = Customer.builder()
        .id(UUID.randomUUID())
        .name("Customer 3")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    customerMap = new HashMap<>();
    customerMap.put(customer1.getId(), customer1);
    customerMap.put(customer2.getId(), customer2);
    customerMap.put(customer3.getId(), customer3);
  }

  @Override
  public Customer getCustomerById(UUID id) {
    log.debug("Get Customer by Id - in service. Id: {}", id.toString());
    return customerMap.get(id);
  }

  @Override
  public List<Customer> getAllCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public Customer saveNewCustomer(Customer customer) {
    Customer savedCustomer = Customer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name(customer.getName())
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    customerMap.put(savedCustomer.getId(), savedCustomer);
    return savedCustomer;
  }
}

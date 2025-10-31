package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.model.CustomerDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class CustomerServiceWithoutDbImpl implements CustomerService {

  private final Map<UUID, CustomerDTO> customerMap;

  public CustomerServiceWithoutDbImpl() {
    CustomerDTO customer1 = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .name("Customer 1")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    CustomerDTO customer2 = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .name("Customer 2")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    CustomerDTO customer3 = CustomerDTO.builder()
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
  public CustomerDTO getCustomerById(UUID id) {
    log.debug("Get Customer by Id - in service. Id: {}", id.toString());
    return Optional.ofNullable(customerMap.get(id)).orElseThrow(NotFoundException::new);
  }

  @Override
  public List<CustomerDTO> getAllCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {
    CustomerDTO savedCustomer = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .name(customer.getName())
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    customerMap.put(savedCustomer.getId(), savedCustomer);
    return savedCustomer;
  }

  @Override
  public void updateCustomerById(UUID customerId, CustomerDTO customer) {
    CustomerDTO existingCustomer = customerMap.get(customerId);
    existingCustomer.setName(customer.getName());
    existingCustomer.setVersion(customer.getVersion());
  }

  @Override
  public void deleteCustomerById(UUID customerId) {
    customerMap.remove(customerId);
  }

  @Override
  public void patchCustomerById(UUID customerId, CustomerDTO customer) {
    CustomerDTO existingCustomer = customerMap.get(customerId);

    if (existingCustomer == null) {
      return;
    }

    if (StringUtils.hasText(customer.getName())) {
      existingCustomer.setName(customer.getName());
    }
  }
}

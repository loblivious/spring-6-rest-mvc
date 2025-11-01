package com.loblivious.spring6restmvc.controller;

import com.loblivious.spring6restmvc.model.CustomerDTO;
import com.loblivious.spring6restmvc.services.CustomerService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController {

  public static final String CUSTOMER_PATH = "/api/v1/customer";
  public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

  private final CustomerService customerService;

  @PatchMapping(CUSTOMER_PATH_ID)
  public ResponseEntity<Void> patchCustomerById(@PathVariable("customerId") UUID customerId,
      @RequestBody CustomerDTO customer) {
    log.info("Patching customer with id {}", customerId);

    customerService.patchCustomerById(customerId, customer);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(CUSTOMER_PATH_ID)
  public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") UUID customerId) {
    log.info("Deleting customer with id {}", customerId);

    customerService.deleteCustomerById(customerId);

    return ResponseEntity.noContent().build();
  }

  @PutMapping(value = CUSTOMER_PATH_ID)
  public ResponseEntity<Void> updateCustomerById(@PathVariable("customerId") UUID customerId,
      @RequestBody CustomerDTO customer) {
    log.info("Received Customer put request: customerId={}, customer={}", customerId, customer);

    customerService.updateCustomerById(customerId, customer);

    return ResponseEntity.noContent().build();
  }

  @PostMapping(CUSTOMER_PATH)
  public ResponseEntity<Void> createCustomer(@RequestBody CustomerDTO customer) {
    log.info("Received Customer post request: {}", customer);

    CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);

    return ResponseEntity.created(URI.create(CUSTOMER_PATH + "/" + savedCustomer.getId())).build();
  }

  @GetMapping(CUSTOMER_PATH)
  public List<CustomerDTO> listCustomers() {
    return customerService.getAllCustomers();
  }

  @GetMapping(CUSTOMER_PATH_ID)
  public CustomerDTO getCustomerById(@PathVariable("customerId") UUID id) {
    log.info("Get Customer by Id - in controller");

    return customerService.getCustomerById(id);
  }
}

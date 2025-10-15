package com.loblivious.spring6restmvc.controller;

import com.loblivious.spring6restmvc.model.Customer;
import com.loblivious.spring6restmvc.services.CustomerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping
  public List<Customer> listCustomers() {
    return customerService.getAllCustomers();
  }

  @GetMapping(value = "{customerId}")
  public Customer getCustomerById(@PathVariable("customerId") UUID id) {
    log.debug("Get Customer by Id - in controller");

    return customerService.getCustomerById(id);
  }

}

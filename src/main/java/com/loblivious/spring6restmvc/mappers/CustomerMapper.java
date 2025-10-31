package com.loblivious.spring6restmvc.mappers;

import com.loblivious.spring6restmvc.entities.Customer;
import com.loblivious.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

  Customer custmerDtoToCustomer(CustomerDTO customerDto);

  CustomerDTO customerToCustomerDto(Customer customer);
}

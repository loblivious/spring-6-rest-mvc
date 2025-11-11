package com.loblivious.spring6restmvc.controller;

import static com.loblivious.spring6restmvc.controller.CustomerController.CUSTOMER_PATH;
import static com.loblivious.spring6restmvc.controller.CustomerController.CUSTOMER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loblivious.spring6restmvc.configs.SpringSecConfig;
import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.model.CustomerDTO;
import com.loblivious.spring6restmvc.services.CustomerService;
import com.loblivious.spring6restmvc.services.CustomerServiceWithoutDbImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
@Import(SpringSecConfig.class)
class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CustomerService customerService;

  CustomerServiceWithoutDbImpl customerServiceWithoutDbImpl;

  @Captor
  ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;

  @BeforeEach
  void setUp() {
    customerServiceWithoutDbImpl = new CustomerServiceWithoutDbImpl();
  }

  @Test
  @SneakyThrows
  void testUpdateCustomerBlankName() {
    CustomerDTO customerDto = customerServiceWithoutDbImpl.getAllCustomers().getFirst();
    customerDto.setName("");

    mockMvc.perform(put(CUSTOMER_PATH_ID, customerDto.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(1)));
  }

  @Test
  @SneakyThrows
  void testCreateEmptyCustomer() {
    CustomerDTO customerDto = CustomerDTO.builder().build();

    mockMvc.perform(post(CUSTOMER_PATH)
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(2)));
  }

  @Test
  @SneakyThrows
  void testGetCustomerByIdNotFound() {
    given(customerService.getCustomerById(any(UUID.class))).willThrow(NotFoundException.class);

    mockMvc.perform(get(CUSTOMER_PATH_ID, UUID.randomUUID())
            .with(jwt()))
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void testPatchCustomer() {
    CustomerDTO customer = customerServiceWithoutDbImpl.getAllCustomers().getFirst();

    Map<String, Object> customerMap = new HashMap<>();
    customerMap.put("name", "John Doe");

    mockMvc.perform(patch(CUSTOMER_PATH_ID, customer.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerMap)))
        .andExpect(status().isNoContent());

    verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(),
        customerArgumentCaptor.capture());

    assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    assertThat(customerMap.get("name")).isEqualTo(customerArgumentCaptor.getValue().getName());
  }

  @Test
  @SneakyThrows
  void testDeleteCustomerById() {
    CustomerDTO customer = customerServiceWithoutDbImpl.getAllCustomers().getFirst();

    mockMvc.perform(delete(CUSTOMER_PATH_ID, customer.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

    assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void testUpdateCustomer() {
    CustomerDTO customer = customerServiceWithoutDbImpl.getAllCustomers().getFirst();

    mockMvc.perform(put(CUSTOMER_PATH_ID, customer.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customer)))
        .andExpect(status().isNoContent());

    verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
  }

  @Test
  @SneakyThrows
  void testCreateNewCustomer() {
    CustomerDTO newCustomer = CustomerDTO.builder().name("New Customer").build();

    given(customerService.saveNewCustomer(any(CustomerDTO.class))).willReturn(
        customerServiceWithoutDbImpl.getAllCustomers().get(1));

    mockMvc.perform(post(CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON)
            .with(jwt())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newCustomer)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

  @Test
  @SneakyThrows
  void testGetAllCustomers() {
    given(customerService.getAllCustomers()).willReturn(
        customerServiceWithoutDbImpl.getAllCustomers());

    mockMvc.perform(get(CUSTOMER_PATH)
            .with(jwt())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)));
  }

  @Test
  @SneakyThrows
  void testGetCustomerById() {
    CustomerDTO testCustomer = customerServiceWithoutDbImpl.getAllCustomers()
        .getFirst();

    given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

    mockMvc.perform(
            get(CUSTOMER_PATH_ID, testCustomer.getId())
                .with(jwt())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
        .andExpect(jsonPath("$.name", is(testCustomer.getName())));
  }
}
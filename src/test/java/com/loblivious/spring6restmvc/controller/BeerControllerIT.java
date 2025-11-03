package com.loblivious.spring6restmvc.controller;

import static com.loblivious.spring6restmvc.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.mappers.BeerMapper;
import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.repositories.BeerRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class BeerControllerIT {

  @Autowired
  private BeerController beerController;

  @Autowired
  private BeerRepository beerRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BeerMapper beerMapper;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  @SneakyThrows
  void testPatchBeerBadName() {
    Beer beer = beerRepository.findAll().getFirst();

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("beerName",
        "New Name 012345678901234567890123456789012345678901234567890123456789");

    mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerMap)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.length()", is(1)));
  }

  @Test
  void testListBeers() {
    List<BeerDTO> dtos = beerController.listBeers();

    assertThat(dtos.size()).isEqualTo(2413);
  }

  @Test
  // if anything fails, rollback to the original state
  @Transactional
  // rollback after the test
  @Rollback
  void testEmptyList() {
    beerRepository.deleteAll();
    List<BeerDTO> dtos = beerController.listBeers();

    assertThat(dtos.size()).isEqualTo(0);
  }

  @Test
  void testGetById() {
    Beer beer = beerRepository.findAll().getFirst();

    BeerDTO beerDto = beerController.getBeerById(beer.getId());

    assertThat(beerDto).isNotNull();
  }

  @Test
  void testGetByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> beerController.getBeerById(UUID.randomUUID()));
  }

  @Test
  @Transactional
  @Rollback
  void testSaveNewBeer() {
    BeerDTO beerDto = BeerDTO.builder().beerName("New Beer").build();

    ResponseEntity<Void> responseEntity = beerController.createBeer(beerDto);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID savedUUID = UUID.fromString(locationUUID[4]);

    assertThat(beerRepository.findById(savedUUID)).isPresent();
  }

  @Test
  @Transactional
  @Rollback
  void testUpdateBeerById() {
    Beer beer = beerRepository.findAll().getFirst();
    BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
    beerDto.setId(null);
    beerDto.setVersion(null);
    final String beerName = "UPDATED";
    beerDto.setBeerName(beerName);

    ResponseEntity<Void> responseEntity = beerController.updateBeerById(beer.getId(), beerDto);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Beer updatedBeer = beerRepository.findById(beer.getId()).orElse(null);
    assertThat(updatedBeer).isNotNull();
    assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
  }

  @Test
  void testUpdateBeerByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> beerController.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build()));
  }

  @Test
  @Transactional
  @Rollback
  void testDeleteById() {
    UUID beerId = beerRepository.findAll().getFirst().getId();

    ResponseEntity<Void> responseEntity = beerController.deleteBeerById(beerId);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    assertThat(beerRepository.findById(beerId)).isNotPresent();

  }

  @Test
  void testDeleteByIdNotFound() {
    assertThrows(NotFoundException.class,
        () -> beerController.deleteBeerById(UUID.randomUUID()));
  }
}
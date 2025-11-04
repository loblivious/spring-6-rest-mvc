package com.loblivious.spring6restmvc.controller;

import static com.loblivious.spring6restmvc.controller.BeerController.BEER_PATH;
import static com.loblivious.spring6restmvc.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.exception.NotFoundException;
import com.loblivious.spring6restmvc.mappers.BeerMapper;
import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.model.BeerStyle;
import com.loblivious.spring6restmvc.repositories.BeerRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  void tesListBeersByStyleAndNameShowInventoryTruePage2() {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .queryParam("beerName", "IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("showInventory", "true")
            .queryParam("page", "2")
            .queryParam("size", "50"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()", is(50)))
        .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
  }

  @Test
  @SneakyThrows
  void testListBeerByStyleAndNameShowInventoryFalse() {
    mockMvc.perform(get(BEER_PATH)
            .queryParam("beerName", BeerStyle.IPA.name())
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("showInventory", Boolean.toString(false))
            .queryParam("size", Integer.toString(1000)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(310)))
        .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
  }

  @Test
  @SneakyThrows
  void testListBeerByStyleAndNameShowInventory() {
    mockMvc.perform(get(BEER_PATH)
            .queryParam("beerName", BeerStyle.IPA.name())
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("showInventory", Boolean.toString(true))
            .queryParam("size", Integer.toString(1000)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(310)))
        .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
  }

  @Test
  @SneakyThrows
  void testListBeerByStyleAndName() {
    mockMvc.perform(get(BEER_PATH)
            .queryParam("beerName", BeerStyle.IPA.name())
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("size", Integer.toString(1000)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(310)));
  }

  @Test
  @SneakyThrows
  void testListBeersByStyle() {
    mockMvc.perform(get(BEER_PATH)
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("size", Integer.toString(1000)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(548)));
  }

  @Test
  @SneakyThrows
  void testListBeersByName() {
    mockMvc.perform(get(BEER_PATH)
            .queryParam("beerName", "IPA")
            .queryParam("size", Integer.toString(1000)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()", is(336)));
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
    Page<BeerDTO> dtos = beerController.listBeers(null, null, null, Pageable.unpaged());

    assertThat(dtos.getContent().size()).isEqualTo(2413);
  }

  @Test
  // if anything fails, rollback to the original state
  @Transactional
  // rollback after the test
  @Rollback
  void testEmptyList() {
    beerRepository.deleteAll();

    Page<BeerDTO> dtos = beerController.listBeers(null, null, null, Pageable.unpaged());

    assertThat(dtos.stream().count()).isEqualTo(0);
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
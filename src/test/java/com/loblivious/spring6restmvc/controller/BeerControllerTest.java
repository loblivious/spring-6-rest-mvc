package com.loblivious.spring6restmvc.controller;

import static com.loblivious.spring6restmvc.controller.BeerController.BEER_PATH;
import static com.loblivious.spring6restmvc.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
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
import com.loblivious.spring6restmvc.model.BeerDTO;
import com.loblivious.spring6restmvc.services.BeerService;
import com.loblivious.spring6restmvc.services.BeerServiceWithoutDbImpl;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BeerController.class)
@Import(SpringSecConfig.class)
class BeerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  BeerService beerService;

  BeerServiceWithoutDbImpl beerServiceWithoutDbImpl;

  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;

  @Captor
  ArgumentCaptor<BeerDTO> beerArgumentCaptor;

  @BeforeEach
  void setUp() {
    beerServiceWithoutDbImpl = new BeerServiceWithoutDbImpl();
  }

  @Test
  @SneakyThrows
  void testUpdateBeerBlankName() {
    BeerDTO beerDto = beerServiceWithoutDbImpl.listBeers(null, Pageable.unpaged()).getContent()
        .getFirst();
    beerDto.setBeerName("");

    mockMvc.perform(put(BEER_PATH_ID, beerDto.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(beerDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(1)));
  }

  @Test
  @SneakyThrows
  void testCreateEmptyBeer() {
    BeerDTO beerDto = BeerDTO.builder().build();

    mockMvc.perform(post(BEER_PATH)
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(6)));
  }

  @Test
  @SneakyThrows
  void testGetBeerByIdNotFound() {

    given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);

    mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID())
            .with(jwt()))
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void testPatchBeer() {
    BeerDTO beerDto = beerServiceWithoutDbImpl.listBeers(null, Pageable.unpaged()).getContent()
        .getFirst();

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("beerName", "New Name");

    mockMvc.perform(patch(BEER_PATH_ID, beerDto.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerMap)))
        .andExpect(status().isNoContent());

    verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

    assertThat(beerDto.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
  }

  @Test
  @SneakyThrows
  void testDeleteBeerById() {
    BeerDTO beerDto = beerServiceWithoutDbImpl.listBeers(null, Pageable.unpaged()).getContent()
        .getFirst();

    mockMvc.perform(delete(BEER_PATH_ID, beerDto.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

    assertThat(beerDto.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void testUpdateBeer() {
    BeerDTO beerDto = beerServiceWithoutDbImpl.listBeers(null, Pageable.unpaged()).getContent()
        .getFirst();

    mockMvc.perform(put(BEER_PATH_ID, beerDto.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(beerDto)))
        .andExpect(status().isNoContent());

    verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));
  }

  @Test
  @SneakyThrows
  void testCreateNewBeer() {
    BeerDTO beerDto = beerServiceWithoutDbImpl.listBeers(null, Pageable.unpaged()).getContent()
        .getFirst();
    beerDto.setVersion(null);
    beerDto.setId(null);

    given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(
        beerServiceWithoutDbImpl.listBeers(null, Pageable.unpaged()).getContent().get(1));

    mockMvc.perform(post(BEER_PATH)
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerDto)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

  @Test
  @SneakyThrows
  void testListBeers() {
    given(beerService.listBeers(any(), any()))
        .willReturn(beerServiceWithoutDbImpl.listBeers(null, Pageable.unpaged()));

    mockMvc.perform(get(BEER_PATH)
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.length()", is(3)));
  }

  @Test
  @SneakyThrows
  void testGetBeerById() {
    BeerDTO testBeerDto = beerServiceWithoutDbImpl.listBeers(null, Pageable.unpaged()).getContent()
        .getFirst();

    given(beerService.getBeerById(testBeerDto.getId())).willReturn(testBeerDto);

    mockMvc.perform(get(BEER_PATH_ID, testBeerDto.getId())
            .with(jwt())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(testBeerDto.getId().toString())))
        .andExpect(jsonPath("$.beerName", is(testBeerDto.getBeerName())));
  }
}
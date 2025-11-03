package com.loblivious.spring6restmvc.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.loblivious.spring6restmvc.model.BeerCSVRecord;
import java.io.File;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class BeerCsvServiceImplTest {

  BeerCsvService beerCsvService = new BeerCsvServiceImpl();

  @Test
  @SneakyThrows
  void convertCSV() {
    File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

    List<BeerCSVRecord> records = beerCsvService.convertCSV(file);

    System.out.println(records.size());

    assertThat(records.size()).isGreaterThan(0);
  }
}
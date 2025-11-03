package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.BeerCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {

  @Override
  public List<BeerCSVRecord> convertCSV(File file) {

    try (var csvFile = new FileReader(file)) {
      return new CsvToBeanBuilder<BeerCSVRecord>(csvFile)
          .withType(BeerCSVRecord.class)
          .build().parse();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }
}

package com.loblivious.spring6restmvc.services;

import com.loblivious.spring6restmvc.model.BeerCSVRecord;
import java.io.File;
import java.util.List;

public interface BeerCsvService {

  List<BeerCSVRecord> convertCSV(File file);
}

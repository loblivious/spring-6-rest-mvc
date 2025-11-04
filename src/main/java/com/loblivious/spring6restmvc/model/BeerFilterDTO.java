package com.loblivious.spring6restmvc.model;

import lombok.Builder;

@Builder
public record BeerFilterDTO(String beerName, BeerStyle beerStyle, Boolean showInventory) {

}

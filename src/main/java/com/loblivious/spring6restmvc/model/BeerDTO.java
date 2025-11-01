package com.loblivious.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerDTO {

  private UUID id;
  private Integer version;

  // Use @NotBlank will be sufficient, but @NotNull provides programmatic feedback
  // that lets us know the input is null
  @NotBlank
  @NotNull
  @Size(max = 50)
  private String beerName;

  @NotNull
  private BeerStyle beerStyle;

  @NotNull
  @NotBlank
  @Size(max = 255)
  private String upc;
  private Integer quantityOnHand;

  @NotNull
  private BigDecimal price;
  private LocalDateTime createdDate;
  private LocalDateTime updateDate;
}

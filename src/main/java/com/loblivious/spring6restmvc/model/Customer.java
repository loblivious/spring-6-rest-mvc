package com.loblivious.spring6restmvc.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Customer {

  private String name;
  private UUID id;
  private Integer version;
  private LocalDateTime createdDate;
  private LocalDateTime updateDate;
}

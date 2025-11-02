package com.loblivious.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerDTO {

  private UUID id;

  @NotNull
  @NotBlank
  private String name;
  private String email;
  private Integer version;
  private LocalDateTime createdDate;
  private LocalDateTime updateDate;
}

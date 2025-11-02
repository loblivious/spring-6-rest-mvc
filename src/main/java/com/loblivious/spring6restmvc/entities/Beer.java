package com.loblivious.spring6restmvc.entities;

import com.loblivious.spring6restmvc.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.CHAR) // tells mysql I want to use varchar as the type for uuid
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;

  @Version
  private Integer version;

  @NotNull
  @NotBlank
  // this does the validation when it hits the db and throws DataIntegrityViolationException
  @Column(length = 50)
  // adding this runs the validation before it hits the db and throws ConstraintViolationException
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

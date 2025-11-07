package com.loblivious.spring6restmvc.entities;

import com.loblivious.spring6restmvc.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
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

  @OneToMany(mappedBy = "beer")
  private Set<BeerOrderLine> beerOrderLines;

  @ManyToMany
  @JoinTable(name = "beer_category",
      joinColumns = @JoinColumn(name = "beer_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  @Builder.Default
  private Set<Category> categories = new HashSet<>();

  public void addCategory(Category category) {
    this.categories.add(category);
    category.getBeers().add(this);
  }

  public void removeCategory(Category category) {
    this.categories.remove(category);
    category.getBeers().remove(this);
  }

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @UpdateTimestamp
  private LocalDateTime updateDate;
}

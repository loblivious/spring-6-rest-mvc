package com.loblivious.spring6restmvc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
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
public class BeerOrder {

  public BeerOrder(UUID id, Long version, LocalDateTime createdDate, LocalDateTime lastModifiedDate,
      String customerRef, Customer customer, Set<BeerOrderLine> beerOrderLines) {
    this.id = id;
    this.version = version;
    this.createdDate = createdDate;
    this.lastModifiedDate = lastModifiedDate;
    this.customerRef = customerRef;
    this.setCustomer(customer);
    this.beerOrderLines = beerOrderLines;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.CHAR) // tells mysql I want to use varchar as the type for uuid
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;

  @Version
  private Long version;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @UpdateTimestamp
  private LocalDateTime lastModifiedDate;

  public boolean isNew() {
    return this.id == null;
  }

  private String customerRef;

  @ManyToOne
  private Customer customer;

  public void setCustomer(Customer customer) {
    this.customer = customer;
    this.customer.getBeerOrders().add(this);
  }

  @OneToMany(mappedBy = "beerOrder")
  private Set<BeerOrderLine> beerOrderLines;

}

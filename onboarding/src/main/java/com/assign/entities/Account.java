package com.assign.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Account {
  @Id
  @GeneratedValue(generator = "account-num-generator")
  @GenericGenerator(
    name = "account-num-generator",
    strategy = "com.assign.entities.accountNumGenerator.AccountNumGenerator"
  )
  private String accountNumber;
  private String accountType = "SAVINGS";
  private BigDecimal balance;
  @ManyToOne
  @JoinColumn(name = "customerId") 
  private Customer customer;
}
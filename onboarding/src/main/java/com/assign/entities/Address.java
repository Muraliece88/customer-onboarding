package com.assign.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String streetName;
  private String houseNumber;
  private String city;
  private String postalCode;
  private String country;
  @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Customer> customer = new HashSet<>();
}

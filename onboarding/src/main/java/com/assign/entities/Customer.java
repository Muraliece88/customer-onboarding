package com.assign.entities;

import com.assign.model.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long customerId;
  @Enumerated(EnumType.STRING)
  private Gender gender;
  private LocalDate birthDate;
  private String nationality;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "addressId")
  private Address address;
  private String bsnNumber;
  private String fullName;
  private String email;
  private String phoneNumber;
  private String passportFilePath;
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,orphanRemoval = true)
  private Set<Account> accounts= new HashSet<>();
  
  
  
}

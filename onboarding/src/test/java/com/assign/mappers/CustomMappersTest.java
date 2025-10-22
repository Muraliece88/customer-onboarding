package com.assign.mappers;

import com.assign.entities.Customer;
import com.assign.model.AddressRequest;
import com.assign.model.Gender;
import com.assign.model.OnboardRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomMappersTest {
  private final CustomMappers mapper = Mappers.getMapper(CustomMappers.class);

  @Test
  void testDtoToCustomerEntity() {
    OnboardRequest onboardRequest = new OnboardRequest(
      "TestUser",
      "MALE",
      LocalDate.of(1990, 1, 1),
      "testabc.com",
      "+31123456789",
      "Dutch",
      new AddressRequest("Main Street", "123", "Almere", "12345AB", "Netherlands"),
      "142442424");

    Customer customer = mapper.dtoToCustomerEntity(onboardRequest);

    assertEquals(onboardRequest.dateOfBirth(), customer.getBirthDate());
    // add more assertions for other mapped fields
  }
}
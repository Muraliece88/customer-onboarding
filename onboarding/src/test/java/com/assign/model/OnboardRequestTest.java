package com.assign.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OnboardRequestTest {
  @Test
  void testOnboardRequest() {
    OnboardRequest onboardRequest = new OnboardRequest(
      "TestUser",
      "MALE",
      LocalDate.of(1990, 1, 1),
      "test@abc.com",
      "+31123456789",
      "Dutch",
      new AddressRequest("Main Street", "123", "Almere", "12345AB", "Netherlands"),
      "142442424");
    assertEquals("TestUser", onboardRequest.fullName());
    assertEquals(LocalDate.of(1990, 1, 1), onboardRequest.dateOfBirth());
    assertEquals("Netherlands", onboardRequest.address().country());
    assertEquals("+31123456789", onboardRequest.phoneNumber());
  }
  
}
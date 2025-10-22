package com.assign.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddressRequestTest {
  @Test
  void testAddressRequest() {
    AddressRequest addressRequest = new AddressRequest(
        "Main Street",
        "123",
        "Almere",
        "12345AB",
        "Netherlands"
    );
    assertEquals("Main Street", addressRequest.streetName());
    assertEquals("123", addressRequest.houseNumber());
    assertEquals("Almere", addressRequest.city());   
    assertEquals("12345AB", addressRequest.postalCode());
    assertEquals("Netherlands", addressRequest.country());
  }

}
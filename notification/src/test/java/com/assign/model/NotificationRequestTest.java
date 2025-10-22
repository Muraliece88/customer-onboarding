package com.assign.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationRequestTest {
  
  @Test
  void testCNotificationRequest() {
    NotificationRequest request = new NotificationRequest(
      "WelcomeTemplate",
      "John Doe",
      "johndoe"

      , "password123",
      "test@gmail.com", List.of("Reason1", "Reason2"));
    assertEquals("WelcomeTemplate", request.emailTemplate());
    assertEquals("John Doe", request.name());
    assertEquals("johndoe", request.loginId());
    assertEquals("password123", request.password());
  }

}
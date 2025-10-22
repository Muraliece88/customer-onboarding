package com.assign.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRequestTest {
  @Test
  void notificationRequestWithCredentials() {
    NotificationRequest notificationRequest = new NotificationRequest(
      "Success",
      "Test test",
      "Testtest",
      "password123",
      "test@yyy.com");
    assertEquals("Success", notificationRequest.emailTemplate());
    assertEquals("Test test", notificationRequest.name());
    assertEquals("Testtest", notificationRequest.loginId());
    assertEquals("password123", notificationRequest.password());
    assertEquals("test@yyy.com", notificationRequest.recipientEmail());
  }
  
  @Test
  void notificationRequestWithFailures() {
    NotificationRequest notificationRequest = new NotificationRequest(
      "Failure",
      "Test test",
      "test@yyy.com",
      List.of("Email missing", "Phone numberNot valid"));
    assertEquals("Failure", notificationRequest.emailTemplate());
    assertEquals("Test test", notificationRequest.name());
    assertEquals("test@yyy.com",notificationRequest.recipientEmail());
    assertNotNull(notificationRequest.failureReasons());
  }

}
package com.assign.model;

import java.util.List;

public record NotificationRequest(String emailTemplate, String name, String loginId, String password, String recipientEmail, List<String> failureReasons) {

  public NotificationRequest(String emailTemplate, String name, String loginId, String password, String recipientEmail) {
    this(emailTemplate, name, loginId, password, recipientEmail, null);
  }
  public NotificationRequest(String emailTemplate, String name, String recipientEmail, List<String> failureReasons) {
    this(emailTemplate, name, null, null, recipientEmail, failureReasons);
  }

  }

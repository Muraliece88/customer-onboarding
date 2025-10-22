package com.assign.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record NotificationRequest(
  @NotBlank (message = "Provide valid template name")String emailTemplate,
  @NotBlank(message = "Provide valid name if name is not known then User must be used") String name, String loginId, String password,
  @NotBlank @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Valid email id is required") 
  String recipientEmail, List<String> failureReasons) {
}

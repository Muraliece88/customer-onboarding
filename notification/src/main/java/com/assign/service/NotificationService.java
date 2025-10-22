package com.assign.service;

import com.assign.model.NotificationRequest;
import jakarta.mail.MessagingException;


public interface NotificationService {
  
  void sendNotification(NotificationRequest notificationRequest, String traceId) throws MessagingException;
  
}

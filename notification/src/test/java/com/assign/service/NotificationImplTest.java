package com.assign.service;

import com.assign.model.NotificationRequest;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationImplTest {
  
  @InjectMocks
  private NotificationImpl notificationImpl;
  @Mock
  private JavaMailSender javaMailSender;
  
  @Mock
  private TemplateEngine templateEngine;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(notificationImpl, "fromAddress", "sample@gmail.com");
  }

  @Test
  void sendSuccessNotification() throws MessagingException {
    NotificationRequest notificationRequest = mock(NotificationRequest.class);
    when(notificationRequest.emailTemplate()).thenReturn("success");
    when(notificationRequest.recipientEmail()).thenReturn("receiver@gmail.com");
    when(templateEngine.process(anyString(),any())).thenReturn("Unable to Onboard");
    when(javaMailSender.createMimeMessage()).thenReturn( mock( MimeMessage.class));
    when(notificationRequest.emailTemplate()).thenReturn("success");
    assertDoesNotThrow(() ->  notificationImpl.sendNotification(notificationRequest,"traceId"));
    
  }
  @Test
  void sendFailureNotification() throws MessagingException {
    NotificationRequest notificationRequest = mock(NotificationRequest.class);
    when(notificationRequest.emailTemplate()).thenReturn("failure");
    when(notificationRequest.recipientEmail()).thenReturn("receiver@gmail.com");
    when(templateEngine.process(anyString(),any())).thenReturn("Unable to Onboard");
    when(javaMailSender.createMimeMessage()).thenReturn( mock( MimeMessage.class));
    assertDoesNotThrow(() ->  notificationImpl.sendNotification(notificationRequest,"traceId"));
   
  

  }

  @Test
  void sendEmail() {
  }
}
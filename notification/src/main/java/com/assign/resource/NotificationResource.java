package com.assign.resource;


import com.assign.model.NotificationRequest;
import com.assign.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationResource {
  
  private final NotificationService notificationService;

  public NotificationResource(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  /**
   * API endpoint to send notification.
   * @param notificationRequest
   * @param traceId
   * @return
   * @throws MessagingException
   */

  @PostMapping(path = "/notify")
  public ResponseEntity<String> successNotification(@Valid @RequestBody NotificationRequest notificationRequest,
                                               @RequestHeader("X-Correlation-Id") String traceId) throws MessagingException {
    notificationService.sendNotification(notificationRequest, traceId);
    return new ResponseEntity<>("Notification sent successfully", HttpStatus.OK);
  }
}

package com.assign.service;


import com.assign.model.NotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Slf4j
@Service
public class NotificationImpl  implements NotificationService {
  private final JavaMailSender emailSender;
  private final TemplateEngine templateEngine;
  
  @Value("${spring.mail.username}")
  private String fromAddress;
  private final String SUCCESS_SUBJECT = "Pleasure to onboard you!";
  private final String FAILURE_SUBJECT = "Unable to complete OnBoarding!";

  public NotificationImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
    this.emailSender = emailSender;
    this.templateEngine = templateEngine;
  }

  /**
   * Domain method to send notification email based on the template type
   * @param notificationRequest
   * @param traceId
   * @throws MessagingException
   */

  @Override
  public void sendNotification(NotificationRequest notificationRequest, String traceId) throws MessagingException {
    Context context = new Context();
    if("success".equalsIgnoreCase(notificationRequest.emailTemplate())) {
      log.info("Preparing success email for the request: {}", traceId);
      context.setVariable("name", notificationRequest.name());
      context.setVariable("username", notificationRequest.loginId());
      context.setVariable("password", notificationRequest.password());
      sendEmail(context, SUCCESS_SUBJECT, notificationRequest.recipientEmail(), "success");
    } else if("failure".equalsIgnoreCase(notificationRequest.emailTemplate())) {
      log.info("Preparing failure email for the request: {}", traceId);
      context.setVariable("name", notificationRequest.name());
      context.setVariable("unmetConditions", notificationRequest.failureReasons());
      sendEmail(context, FAILURE_SUBJECT, notificationRequest.recipientEmail(), "failure");
    }
    log.info("Email has been sent for the request: {}", traceId);
  }
  
  public void sendEmail(Context context, String subject, String receiverEmail, String template) throws MessagingException {
    MimeMessage mimeMessage = emailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
    mimeMessageHelper.setFrom(fromAddress);
    mimeMessageHelper.setTo(receiverEmail);
    mimeMessageHelper.setSubject(subject);
    String body = templateEngine.process(template, context);
    mimeMessageHelper.setText(body, true);
    emailSender.send(mimeMessage);
  }
}

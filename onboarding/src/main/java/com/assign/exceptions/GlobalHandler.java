package com.assign.exceptions;


import com.assign.utils.NotificationProcessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintDefinitionException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/*
  * Global exception handler for the application
 */
@Slf4j
@RestControllerAdvice
public class GlobalHandler {
  private final NotificationProcessor notificationProcessor;

  public GlobalHandler(NotificationProcessor notificationProcessor) {
    this.notificationProcessor = notificationProcessor;
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<OperationError> handleException(Exception ex, HttpServletRequest request) throws NoSuchAlgorithmException {
    var email = Optional.ofNullable(request.getAttribute("onboardRequest")).map(Object::toString).orElse("");
    String EMAIL_TEMPLATE = "failure";
    String CUSTOMER = "Customer";
    String notification_success_message="Unable to process your request due to incorrect/missing data. Please check your email for more details.";
    String notification_failure_message="Sorry unable to process your request for the above reasons and could not email the details. Please contact support";
    if (ex instanceof CustomerExistsException || 
        ex instanceof ConstraintViolationException || 
        ex instanceof MissingServletRequestParameterException ||
        ex instanceof MethodArgumentTypeMismatchException ||
        ex instanceof ConstraintDefinitionException) {
     if( notificationProcessor.notifyCustomer(
        EMAIL_TEMPLATE, CUSTOMER,email, List.of(ex.getMessage()), UUID.randomUUID().toString()))
     {
       return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(),notification_success_message);
     }
     else  {
       return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(),notification_failure_message);
     }
      
    }
    else if(ex instanceof MethodArgumentNotValidException){
      String errorMessage = String.valueOf(((MethodArgumentNotValidException) ex).
        getBindingResult().getFieldErrors().stream().
       map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
      if(notificationProcessor.notifyCustomer(
        EMAIL_TEMPLATE, CUSTOMER,email, List.of(errorMessage), UUID.randomUUID().toString()))
      {
        return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value(),notification_success_message);
      }
      else {
        return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value(),notification_failure_message);
      }
  }
    else if(ex instanceof HandlerMethodValidationException){
      String errorMessage = ((HandlerMethodValidationException) ex)
        .getParameterValidationResults().stream()
        .flatMap(result -> result.getResolvableErrors().stream())
        .map(MessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
        if(notificationProcessor.notifyCustomer(
          EMAIL_TEMPLATE, CUSTOMER,email, List.of(errorMessage), UUID.randomUUID().toString()))
        {
          return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value(), notification_success_message);
        }
        else  {
          return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value(),notification_failure_message);
        }
    } else if (ex instanceof NotificationException) {
      return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

    }
      
    else {
      if(notificationProcessor.notifyCustomer(
        EMAIL_TEMPLATE, CUSTOMER,email, List.of(ex.getMessage()), UUID.randomUUID().toString()))
      {
        return buildErrorResponse("Technical Error occured while processing request ", HttpStatus.INTERNAL_SERVER_ERROR.value(), notification_success_message);
      }
      else
      {
        return buildErrorResponse("Technical Error occured while processing request ", HttpStatus.INTERNAL_SERVER_ERROR.value(),notification_failure_message);
      }
    
  }
    
  }

  /**
   * Builds the error response entity
   * @param reasons
   * @param statusCode
   * @param message
   * @return
   */
  private ResponseEntity<OperationError> buildErrorResponse(String  reasons, int statusCode, String message) {
    log.error("Exception occured while processing the request: {}", reasons);
    OperationError error = new OperationError(statusCode,
      Arrays.stream(reasons.split(","))
        .map(msg -> msg.replaceAll("[\\[\\]]", "").trim())
        .collect(Collectors.toList()), Instant.now(), message);
    return new ResponseEntity<>(error, HttpStatusCode.valueOf(statusCode));
  }
}


package com.assign.exceptions;


import jakarta.mail.MessagingException;
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
import java.util.stream.Collectors;

/**
 * Global exception handler to handle various exceptions across the application.
 */
@Slf4j
@RestControllerAdvice
public class GlobalHandler {
  
  @ExceptionHandler(Exception.class)
  public ResponseEntity<NotificationError> handleException(Exception ex, HttpServletRequest request) throws NoSuchAlgorithmException {
    
    if (ex instanceof ConstraintViolationException || 
        ex instanceof MissingServletRequestParameterException ||
        ex instanceof MethodArgumentTypeMismatchException ||
        ex instanceof ConstraintDefinitionException) {
      return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
    else if(ex instanceof MethodArgumentNotValidException){
      String errorMessage = String.valueOf(((MethodArgumentNotValidException) ex).
        getBindingResult().getFieldErrors().stream().
       map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
      return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value());
  }
    else if(ex instanceof HandlerMethodValidationException){
      String errorMessage = ((HandlerMethodValidationException) ex)
        .getParameterValidationResults().stream()
        .flatMap(result -> result.getResolvableErrors().stream())
        .map(MessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
      return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value());
    }
    else if(ex instanceof MessagingException){
      return buildErrorResponse("Technical Error occured while processing request ", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    else {
     return buildErrorResponse("Technical Error occured while processing request ", HttpStatus.INTERNAL_SERVER_ERROR.value());
  }
    
  }

  private ResponseEntity<NotificationError> buildErrorResponse(String  message, int statusCode) {
    log.error("Exception occured while processing the request: {}", message);
    NotificationError error = new NotificationError(statusCode,
      Arrays.stream(message.split(","))
        .map(msg -> msg.replaceAll("[\\[\\]]", "").trim())
        .collect(Collectors.toList()), Instant.now());
    return new ResponseEntity<>(error, HttpStatusCode.valueOf(statusCode));
  }
}


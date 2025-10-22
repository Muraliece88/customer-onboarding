package com.assign.exceptions;

import com.assign.utils.NotificationProcessor;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalHandlerTest {
  @InjectMocks
  private GlobalHandler globalHandler;
  @Mock
  private NotificationProcessor notificationProcessor;
  
  @Test
  void testHandleCustomerExistException() throws NoSuchAlgorithmException {
    CustomerExistsException customerExistsException = mock(CustomerExistsException.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(customerExistsException.getMessage()).thenReturn("customer already exists");
    when(notificationProcessor.notifyCustomer(anyString(), anyString(), anyString(),any(List.class), anyString())).thenReturn(true);
    ResponseEntity<OperationError> errorResponseEntity = globalHandler.handleException(customerExistsException,request);
    assertEquals(HttpStatus.BAD_REQUEST, errorResponseEntity.getStatusCode());
    assertEquals("customer already exists", errorResponseEntity.getBody().reasons().stream().map(String::toString).findFirst().get());
   
  }
  @Test
  void testHandleMethodArgumentNotValidExceptionException() throws NoSuchAlgorithmException {
    MethodArgumentNotValidException methodNotValidException = mock(MethodArgumentNotValidException.class);
    BindingResult bindingResult = mock( BindingResult.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(methodNotValidException.getBindingResult()).thenReturn(bindingResult);
    when(notificationProcessor.notifyCustomer(anyString(), anyString(), anyString(),any(List.class), anyString())).thenReturn(true);
    ResponseEntity<OperationError> errorResponseEntity = globalHandler.handleException(methodNotValidException,request);
    assertEquals(HttpStatus.BAD_REQUEST, errorResponseEntity.getStatusCode());

  }
  @Test
  void testHandleGenericException() throws NoSuchAlgorithmException {
    Exception exception = mock(Exception.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(exception.getMessage()).thenReturn("exception message");
    when(notificationProcessor.notifyCustomer(anyString(), anyString(), anyString(),any(List.class), anyString())).thenReturn(true);
    ResponseEntity<OperationError> errorResponseEntity = globalHandler.handleException(exception, request);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponseEntity.getStatusCode());
  }

}
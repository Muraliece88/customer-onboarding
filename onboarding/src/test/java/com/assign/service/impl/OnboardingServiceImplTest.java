package com.assign.service.impl;

import com.assign.entities.Customer;
import com.assign.exceptions.CustomerExistsException;
import com.assign.mappers.CustomMappers;
import com.assign.model.OnboardRequest;
import com.assign.repository.CustomerRepo;
import com.assign.utils.FileProcessor;
import com.assign.utils.NotificationProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OnboardingServiceImplTest {
  @InjectMocks
  private OnboardingServiceImpl onboardingServiceImpl;
  @Mock
  private CustomerRepo customerRepo;
  @Mock
  private FileProcessor fileProcessor;
  @Mock
  private CustomMappers customMappers;
  @Mock
  private NotificationProcessor notificatIonProcessor;

  @Test
  public void shouldOnboardCustomerFail() throws IOException, NoSuchAlgorithmException {
    OnboardRequest onboardRequest = mock(OnboardRequest.class);
    Customer customer = mock(Customer.class);
    MultipartFile file = mock(MultipartFile.class);
    when(customerRepo.findByEmail(anyString())).thenReturn(Optional.of(customer));
    when(onboardRequest.email()).thenReturn("test@sample.com");


    assertThrows(CustomerExistsException.class, () -> {
      onboardingServiceImpl.onboardCustomer(onboardRequest,file,"traceId");
    });
    
  }
  @Test
  public void shouldOnboardCustomerSuccesss() throws IOException, NoSuchAlgorithmException {
    OnboardRequest onboardRequest = mock(OnboardRequest.class);
    Customer customer = mock(Customer.class);
    MultipartFile file = mock(MultipartFile.class);
    when(onboardRequest.email()).thenReturn("sample@test.com");
    when(customerRepo.findByEmail(anyString())).thenReturn(Optional.empty());
    when(file.getOriginalFilename()).thenReturn("sample.png");
    doNothing().when(fileProcessor).processFile(file, null, "png", onboardRequest);
    when(customMappers.dtoToCustomerEntity(onboardRequest)).thenReturn(customer);
    when(notificatIonProcessor.notifyCustomer("success", "test", "sample@test.com", null, "traceId"))
      .thenReturn(true);
    when(onboardRequest.fullName()).thenReturn("test");
    String result =onboardingServiceImpl.onboardCustomer(onboardRequest,file,"traceId");
    assertEquals(result, "Welcome Onboard test please check your email for further instructions");
    
  }
}
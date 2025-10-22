package com.assign.resource;

import com.assign.model.AddressRequest;
import com.assign.model.Gender;
import com.assign.model.OnboardRequest;
import com.assign.service.OnboardingService;
import com.assign.utils.NotificationProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OnboardingResourceTest {
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @MockitoBean
  private OnboardingService onboardingService;
  @MockitoBean
  private NotificationProcessor notificationProcessor;
  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
      .build();
  }
  @Test
  void onboardCustomerSuccess() throws Exception {

    OnboardRequest onboardRequest = new OnboardRequest(
      "TestUser",
      "MALE",
      LocalDate.of(1990, 1, 1),
      "test@abc.com",
      "+31123456789",
      "Dutch",
      new AddressRequest("Main Street", "123", "Almere", "12345AB", "Netherlands"),
      "142442424");
    ObjectMapper objectMapper = context.getBean(ObjectMapper.class);

    MockMultipartFile onboardRequestPart = new MockMultipartFile(
      "onboardRequest",
      "",
      MediaType.APPLICATION_JSON_VALUE,
      objectMapper.writeValueAsBytes(onboardRequest)
    );

    MockMultipartFile passportPart = new MockMultipartFile(
      "passport",
      "passport.jpg",
      MediaType.IMAGE_JPEG_VALUE,
      "dummy-image-content".getBytes()
    );
    when(onboardingService.onboardCustomer(any(), any(), anyString()))
      .thenReturn("Success");
    mockMvc.perform(
      org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/api/v1/customer-onboarding")
        .file(onboardRequestPart)
        .file(passportPart)
    ).andExpect(status().isCreated());
  }
  @Test
  void onboardCustomerBadRequest() throws Exception {

    OnboardRequest onboardRequest = new OnboardRequest(
      "TestUser",
      "MALE",
      LocalDate.of(1990, 1, 1),
      "testabc.com",
      "+31123456789",
      "Dutch",
      new AddressRequest("Main Street", "123", "Almere", "12345AB", "Netherlands"),
      "142442424");
    ObjectMapper objectMapper = context.getBean(ObjectMapper.class);

    MockMultipartFile onboardRequestPart = new MockMultipartFile(
      "onboardRequest",
      "",
      MediaType.APPLICATION_JSON_VALUE,
      objectMapper.writeValueAsBytes(onboardRequest)
    );
    MockMultipartFile passportPart = new MockMultipartFile(
      "passport",
      "passport.jpg",
      MediaType.IMAGE_JPEG_VALUE,
      "dummy-image-content".getBytes()
    );
   when(notificationProcessor.notifyCustomer(anyString(), anyString(), anyString(),any(List.class), anyString())).thenReturn(true);
     
    
    mockMvc.perform(
      org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/api/v1/customer-onboarding")
        .file(onboardRequestPart)
        .file(passportPart)
    ).andExpect(status().isBadRequest()).andExpect(jsonPath("$.reasons").value("Valid email id is required"));
  }
}
package com.assign.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificationResourceTest {
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
      .build();
  }
  @Test
  void successNotification() throws Exception {
    String request="\n" +
      "{\n" +
      "  \"emailTemplate\": \"Success\",\n" +
      "  \"name\": \"John Doe\",\n" +
      "  \"loginId\": \"johndoe123\",\n" +
      "  \"password\": \"securePassword123!\",\n" +
      "  \"recipientEmail\": \"john.doe@example.com\"\n" +
      "}";
    mockMvc.perform(post("/api/v1/notifications/notify")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request)
            .header("X-Correlation-Id", "test-trace-id"))
      .andExpect(status().isOk());
    
  }

  @Test
  void validationFailure() throws Exception {
    String request="\n" +
      "{\n" +
      "  \"emailTemplate\": \"success\",\n" +
      "  \"name\": \"John Doe\",\n" +
      "  \"loginId\": \"johndoe123\",\n" +
      "  \"password\": \"securePassword123!\",\n" +
      "  \"recipientEmail\": \"john.doeexample.com\"\n" +
      "}";
    mockMvc.perform(post("/api/v1/notifications/notify")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request)
        .header("X-Correlation-Id", "test-trace-id"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Valid email id is required"));

  }
}
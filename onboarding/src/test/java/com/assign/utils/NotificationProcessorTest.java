package com.assign.utils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationProcessorTest {
  @Mock
  private DiscoveryClient discoveryClient;
  @Mock
  private WebClient.Builder webClientBuilder;
  @Mock
  private WebClient webClient;
  @Mock
  private WebClient.RequestBodyUriSpec requestBodyUriSpec;
  @Mock
  private WebClient.RequestHeadersSpec requestHeadersSpec;

  @InjectMocks
  private NotificationProcessor notificationProcessor;
  @Mock
  private ServiceInstance serviceInstance;
  private MockedStatic<WebClient> webClientStaticMock;

  @BeforeEach
  void setUp() {
    webClientStaticMock = Mockito.mockStatic(WebClient.class);
    webClientStaticMock.when(WebClient::builder).thenReturn(webClientBuilder);

    ReflectionTestUtils.setField(notificationProcessor, "apiName", "notification-service");
    ReflectionTestUtils.setField(notificationProcessor, "notificationBasePath", "/api/v1");
    ReflectionTestUtils.setField(notificationProcessor, "apiEndpoint", "/notify");
  }

  @AfterEach
  void tearDown() {
    webClientStaticMock.close();
  }

  @Test
  void shouldNotifyCustomerSuccess() throws NoSuchAlgorithmException {
    when(discoveryClient.getInstances("notification-service")).thenReturn(List.of(serviceInstance));
    when(serviceInstance.getUri()).thenReturn(URI.create("http://localhost:8080"));
    when(webClientBuilder.baseUrl("http://localhost:8080/api/v1")).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(webClient);
    when(webClientBuilder.defaultHeaders(any())).thenReturn(webClientBuilder);
    when(webClient.post()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.uri(Mockito.<Function<UriBuilder, URI>>any())).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.headers(any())).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.accept(any(MediaType.class))).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.exchangeToMono(any())).thenReturn(Mono.just("200"));

    assertDoesNotThrow(() -> notificationProcessor.notifyCustomer(
      "success", "test", "sample@test.com", List.of("data1", "data2"), "traceId"
    ));
  }
  @Test
  void shouldNotifyCustomerFail() throws NoSuchAlgorithmException {
    when(discoveryClient.getInstances("notification-service")).thenReturn(List.of(serviceInstance));
    when(serviceInstance.getUri()).thenReturn(URI.create("http://localhost:8080"));
    when(webClientBuilder.baseUrl("http://localhost:8080/api/v1")).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(webClient);
    when(webClient.post()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.uri(Mockito.<Function<UriBuilder, URI>>any())).
      thenReturn(requestBodyUriSpec);
    when(webClientBuilder.defaultHeaders(any())).thenReturn(webClientBuilder);
    when(requestBodyUriSpec.headers(any())).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.accept(any(MediaType.class))).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.exchangeToMono(any())).thenReturn(Mono.just("500"));
    assertDoesNotThrow(() -> notificationProcessor.notifyCustomer(
      "success", "test", "sample@test.com", List.of("data1", "data2"), "traceId"
    ));
   
  }

 
}
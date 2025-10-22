package com.assign.utils;


import com.assign.exceptions.URINotFoundException;
import com.assign.model.NotificationRequest;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;


/***
 * * Utility class to process notification requests
 */

@Slf4j
@Component
public class NotificationProcessor {
  private final DiscoveryClient discoveryClient;
  @Value("${notification.service.base.path}")
  private String notificationBasePath;
  @Value("${notification.api.name}")
  private String apiName;
  @Value("${notification.service.endpoint}")
  private String apiEndpoint;
  @Value("${notification.service.userName}")
  private String apiUserName;
  @Value("${notification.service.password}")
  private String apiPassword;
  public static final String CORRELATION_ID="X-Correlation-Id";
  private final String SUCCESS_TEMPLATE="success";
  

  public NotificationProcessor(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }


  /**
   * Domain method to notify customer based on the email template
   * @param emailTemplate
   * @param name
   * @param recipientEmail
   * @param failureReasons
   * @param traceId
   * @return
   * @throws NoSuchAlgorithmException
   */
  public boolean notifyCustomer(String emailTemplate,String name,
                      String recipientEmail,List<String> failureReasons,String traceId) throws NoSuchAlgorithmException {
    NotificationRequest notificationRequest;
    if(SUCCESS_TEMPLATE.equalsIgnoreCase(emailTemplate))
    {
      notificationRequest = createNotificationRequest(emailTemplate,name,recipientEmail);
      log.info("Preparing success notification for requestId: {}" , traceId );
    }
    else {
      notificationRequest=  createNotificationRequest(emailTemplate,name,recipientEmail,failureReasons);
      log.info("Preparing failure notification for requestId: {}" , traceId );
    }
    
    String response =  fetchProxyDetails(discoveryClient).post().uri(uriBuilder -> uriBuilder.
        path(apiEndpoint).build()).
      headers(httpHeaders ->{
        httpHeaders.set(CORRELATION_ID, traceId);
      })
      .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)).
      bodyValue(notificationRequest).exchangeToMono(
        clientResponse ->
          Mono.just(String.valueOf(clientResponse.statusCode().value())))
      .doOnError(e-> {
        log.error("Technical Error when notifying customer");
      }).block();
    if ("200".equals(response)) {
      log.info("Customer notification successful for requestId: {}", traceId);
      return true;
    } else {
      log.error("Unable to notify customer for requestId: {}", traceId);
      return false;
    }
  }

  /**Method to fetch notification service details from Eureka server
   * 
   * @param discoveryClient
   * @return
   */

  public WebClient fetchProxyDetails(DiscoveryClient discoveryClient)
  {
    String hostPort= discoveryClient.getInstances(apiName).
      stream().map(serviceInstance -> serviceInstance.getUri().toString())
      .findAny().orElseThrow(()-> new URINotFoundException("No such uri available"));
    return getWebClient(hostPort+notificationBasePath, apiUserName, apiPassword);

  }

  /**
   * Method to create web client with basic authentication
   * @param notificationBasePath
   * @param apiUser
   * @param apiPass
   * @return
   */

  private WebClient getWebClient(String notificationBasePath,String apiUser, String apiPass) {
    String encoded= Base64.getEncoder().
      encodeToString((apiUser+":"+apiPass).getBytes(StandardCharsets.UTF_8));
    return WebClient.builder().
      baseUrl(notificationBasePath)
     .defaultHeaders(header->header.setBasicAuth(encoded))
      .build();
  }

    /**
     * Method to create build notification request
     * @param emailTemplate
     * @param name
     * @param recipientEmail
     * @return
     * @throws NoSuchAlgorithmException
     */
  
  private NotificationRequest createNotificationRequest(String emailTemplate,
                                                        String name,
                                                        String recipientEmail) throws NoSuchAlgorithmException {
    
      return new NotificationRequest(emailTemplate, name, recipientEmail.substring(0, recipientEmail.indexOf('@')), generatePassword(), recipientEmail);
  }
  private NotificationRequest createNotificationRequest(String emailTemplate,
                                                        String name,
                                                        String recipientEmail, List<String> failureReason) {


    return new NotificationRequest(emailTemplate,name,recipientEmail,failureReason);
  }

    /**
     * Custom logic to generate onetime password
     * @return
     * @throws NoSuchAlgorithmException
     */
  private String generatePassword() throws NoSuchAlgorithmException {

    SecureRandom random = SecureRandom.getInstanceStrong();
    String specials = "@#$%&*!";
    String pool = "abcdefghijklmnopqrstuvwxyz0123456789" + specials;

    String firstChar = String.valueOf((char) ('A' + random.nextInt(26)));

    String restCharacters = random.ints(7, 0, pool.length())
      .mapToObj(pool::charAt)
      .map(String::valueOf)
      .reduce("", String::concat);

    return firstChar + restCharacters;
  }
}

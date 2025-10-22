package com.assign.resource;


import com.assign.model.OnboardRequest;
import com.assign.service.OnboardingService;
import com.assign.validators.ValidFile;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Contoller class for customer onboarding
 */
@RestController
@RequestMapping("/api/v1/customer-onboarding")
public class OnboardingResource {
  
  private final OnboardingService onboardingService;

  public OnboardingResource(OnboardingService onboardingService) {
    this.onboardingService = onboardingService;
  }

  /**
   * API to onboard customer
   * @param onboardRequest
   * @param passportImage
   * @return
   * @throws IOException
   * @throws NoSuchAlgorithmException
   */
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> onboardCustomer(@Valid @RequestPart("onboardRequest") OnboardRequest onboardRequest,
                                                @ValidFile @RequestPart("passport")  MultipartFile passportImage) throws IOException, NoSuchAlgorithmException {
    
    return new ResponseEntity<>(
      onboardingService.onboardCustomer(onboardRequest, passportImage, UUID.randomUUID().toString()),
      HttpStatus.CREATED
);

  }
  
}

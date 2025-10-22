package com.assign.service.impl;

import com.assign.entities.Account;
import com.assign.entities.Customer;
import com.assign.exceptions.CustomerExistsException;
import com.assign.exceptions.NotificationException;
import com.assign.mappers.CustomMappers;
import com.assign.model.OnboardRequest;
import com.assign.repository.CustomerRepo;
import com.assign.service.OnboardingService;
import com.assign.utils.FileProcessor;
import com.assign.utils.NotificationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Service implementation for customer onboarding
 */

@Service
@Slf4j
public class OnboardingServiceImpl  implements OnboardingService {

  private final CustomerRepo customerRepo;
  private final CustomMappers customMappers;
  @Value("${passportFile.upload-path}")
  private String uploadDir;
  private final NotificationProcessor notificatIonProcessor;
  private final FileProcessor fileProcessor;
  

  public OnboardingServiceImpl(CustomerRepo customerRepo, CustomMappers customMappers, NotificationProcessor notificatIonProcessor, FileProcessor fileProcessor) {
    this.customerRepo = customerRepo;
    this.customMappers = customMappers;
    this.notificatIonProcessor = notificatIonProcessor;

    this.fileProcessor = fileProcessor;
  }


  /**
   * Domain method to handle customer onboarding business logic
   * @param onboardRequest
   * @param passportImage
   * @param traceId
   * @return
   * @throws IOException
   * @throws NoSuchAlgorithmException
   */
  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public String onboardCustomer(OnboardRequest onboardRequest, MultipartFile passportImage,String traceId) throws IOException, NoSuchAlgorithmException {
    log.info("OnboardCustomer process has been initiated for email: {} with traceId: {}", onboardRequest.email(), traceId);
    customerRepo.findByEmail(onboardRequest.email()).ifPresent(customer -> {
      log.info("Customer with email {} already exists", onboardRequest.email());
      throw new CustomerExistsException("Dear customer there is an existing account registered with the same email.");
    });
    
    String extension = Optional.ofNullable(passportImage.getOriginalFilename())
      .filter(name -> name.contains("."))
      .map(name -> name.substring(name.lastIndexOf('.') + 1))
      .orElse("");
    
    fileProcessor.processFile(passportImage, uploadDir, extension, onboardRequest);
    Customer customerEntity  = customMappers.dtoToCustomerEntity(onboardRequest);
    customerEntity.setPassportFilePath(uploadDir + "/" + onboardRequest.bsnNumber() + "." + extension);
    Account account = new Account();
    account.setBalance(BigDecimal.ZERO);
    account.setCustomer(customerEntity);
    customerEntity.getAccounts().add(account);
    customerRepo.save(customerEntity);
    log.info("Customer has been onboarded successfully with traceId: {}", traceId);
    if(notificatIonProcessor.notifyCustomer("success", onboardRequest.fullName(),
      onboardRequest.email(),null, traceId)) {
      return "Welcome Onboard %s please check your email for further instructions".formatted(onboardRequest.fullName());
    }
    else
    {
      throw new NotificationException("Onboarding is successful but we are unable to send email about instructions to login to your account." +
        "Please contact support");
    }
  }
}

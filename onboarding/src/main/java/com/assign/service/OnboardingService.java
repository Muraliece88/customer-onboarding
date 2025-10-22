package com.assign.service;

import com.assign.model.OnboardRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public interface OnboardingService {
  String onboardCustomer(OnboardRequest onboardRequest, MultipartFile file,String traceId) throws IOException, NoSuchAlgorithmException;
}

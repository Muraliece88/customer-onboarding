package com.assign.utils;

import com.assign.model.OnboardRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileProcessorTest {
  @Mock
  private FileProcessor fileProcessor;


  @Test
  void processFile() throws IOException {
    MultipartFile passportImage = mock(MultipartFile.class);
    OnboardRequest onboardRequest = mock(OnboardRequest.class);
    fileProcessor.processFile(passportImage, "/usr", "png", onboardRequest);
    verify(fileProcessor,times(1)).processFile(passportImage,"/usr","png",onboardRequest);
   
  }
}
package com.assign.utils;


import com.assign.model.OnboardRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Component
public class FileProcessor {
  
  public void processFile(MultipartFile passportImage, String uploadDir,String extension, OnboardRequest onboardRequest) throws IOException {
    
    Path path = Path.of(uploadDir);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
    passportImage.transferTo(Path.of(uploadDir, onboardRequest.bsnNumber() + "." + extension));
  }
  
}

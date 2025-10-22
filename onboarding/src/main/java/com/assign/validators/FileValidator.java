package com.assign.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

  @Override
  public void initialize(ValidFile constraintAnnotation) {

  }

  @Override
  public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {

    if (!multipartFile.isEmpty()) {
      return isSupportedContentType(Objects.requireNonNull(multipartFile.getContentType()));
    }
    return false;
  }

  private boolean isSupportedContentType(String contentType) {
    return contentType.equals("text/xml")
      || contentType.equals("image/png")
      || contentType.equals("image/jpg")
      || contentType.equals("image/jpeg");
  }
}

package com.assign.model;


import com.assign.validators.EnumValid;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OnboardRequest(@NotBlank(message = "firstname and last name is missing") String fullName,
                             @NotBlank(message = "Gender is missing")
                             @EnumValid(enumClass = Gender.class, message = "Valid gender are:  MALE, FEMALE,OTHER") String gender,
                             @NotNull(message = "Date of Birth is missing")
                             @NotNull @JsonFormat(pattern = "yyyy-MM-dd")LocalDate dateOfBirth,
                             @NotBlank @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
                               flags = Pattern.Flag.CASE_INSENSITIVE, message = "Valid email id is required")String email,
                             @NotBlank @Pattern(regexp = "^\\+31\\d{9}$", message = "Phone number must be in the format +31 followed by 9 digits")String phoneNumber,
                             @NotBlank (message = "Nationality of the account holder is missing")String nationality,
                             AddressRequest address,
                             @NotBlank (message = "BSN number is missing")String bsnNumber) {
}

package com.assign.mappers;


import com.assign.entities.Customer;
import com.assign.model.OnboardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Custom mapping configuration
 */
@Mapper(componentModel = "spring")
public interface CustomMappers {
  
  @Mapping(target = "birthDate", source = "dateOfBirth")
  Customer dtoToCustomerEntity(OnboardRequest onboardRequest);
}

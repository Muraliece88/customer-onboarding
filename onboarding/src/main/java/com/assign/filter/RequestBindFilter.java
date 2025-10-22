package com.assign.filter;

import com.assign.model.OnboardRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.IOException;

/**
 * Filter classs needed to make email information available for the exception handler
 */
@Component
public class RequestBindFilter  extends OncePerRequestFilter {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
    throws ServletException, IOException {

    if (request.getContentType() != null &&
      request.getContentType().startsWith("multipart/form-data")) {

      MultipartHttpServletRequest multipartRequest = new StandardMultipartHttpServletRequest(request);
      String ATTRIBUTE_NAME = "onboardRequest";
      String json = multipartRequest.getParameter(ATTRIBUTE_NAME);

      if (json != null) {
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        OnboardRequest onboardRequest = objectMapper.readValue(json, OnboardRequest.class);
        request.setAttribute(ATTRIBUTE_NAME, onboardRequest.email());
        filterChain.doFilter(multipartRequest, response);
      } else {
        filterChain.doFilter(request, response);
      }
    }
  }
}

 
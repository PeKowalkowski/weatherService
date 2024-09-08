package com.example.weatherService.controllers;

import com.example.weatherService.models.UserRegistrationDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

public interface AuthController {

  @PostMapping("/sign-up")
  public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRegistrationDto userRegistrationDto2);

  @PostMapping("/sign-in")
  public ResponseEntity<?> authenticateUser(Authentication authentication, HttpServletResponse response);

  @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
  @PostMapping ("/refresh-token")
  public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

  @PostMapping(path = "/forgotPassword")
  ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);
}

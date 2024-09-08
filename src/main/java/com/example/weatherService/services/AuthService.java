package com.example.weatherService.services;

import com.example.weatherService.models.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AuthService {
  boolean registerUser(String token, String password);

  public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response);

  Object getAccessTokenUsingRefreshToken(String authorizationHeader);

  ResponseEntity<String> forgotPassword(Map<String, String> requestMap);
}

package com.example.weatherService.controllersImpl;

import com.example.weatherService.WeatherConstans.WeatherConstans;
import com.example.weatherService.controllers.AuthController;
import com.example.weatherService.exceptions.UserAlreadyExistException;
import com.example.weatherService.models.UserRegistrationDto;
import com.example.weatherService.services.AuthService;
import com.example.weatherService.utils.WeatherUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImpl implements AuthController {

  private final AuthService authService;

  @Override
  public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRegistrationDto registrationDto2) {

    try {
      String token2 = String.valueOf(authService.registerUser(registrationDto2.getToken(), registrationDto2.getPassword()));
      return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("token2", token2));
    } catch (UserAlreadyExistException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "The user with the provided email already exists"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap(  "User registration failed\"", e.getMessage()));
    }
  }

  @Override
  public ResponseEntity<?> authenticateUser(Authentication authentication, HttpServletResponse response) {
    return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication, response));
  }

  @Override
  public ResponseEntity<?> getAccessToken(String authorizationHeader) {
    return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
  }

  @Override
  public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
    try {
      return authService.forgotPassword(requestMap);
    }catch (Exception ex) {
      ex.printStackTrace();
      return WeatherUtils.getResponseEntity(WeatherConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
  }
}

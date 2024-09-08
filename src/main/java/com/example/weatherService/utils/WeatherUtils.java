package com.example.weatherService.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WeatherUtils {

  public static ResponseEntity<String> getResponseEntity(String message, HttpStatus status, Exception e) {
    return ResponseEntity.status(status).body(message + ": " + e.getMessage());
  }
}

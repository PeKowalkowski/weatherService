package com.example.weatherService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistException extends RuntimeException {

  public UserAlreadyExistException(String email) {
    super("User with email " + email + " already exists.");
  }
}

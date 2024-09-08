package com.example.weatherService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api")
public interface InvitationController {

  @PostMapping("/invite/{recipientEmail}")
  public ResponseEntity<Map<String, String>> invite(@PathVariable String recipientEmail);


}

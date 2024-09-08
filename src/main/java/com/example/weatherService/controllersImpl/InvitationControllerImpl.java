package com.example.weatherService.controllersImpl;

import com.example.weatherService.controllers.InvitationController;
import com.example.weatherService.repositories.UserRepo;
import com.example.weatherService.services.MailService;
import com.example.weatherService.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class InvitationControllerImpl implements InvitationController {

  private final MailService mailService;
  private final UserRepo userRepo;
  /*@Override
  public ResponseEntity<Map<String, String>>invite(@PathVariable String recipientEmail) {

    try {
      Map<String, String> response = mailService.sendInvitationEmail(recipientEmail);
      return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Invitation email sent successfully", response.toString()));
    } catch (Exception e) {
      String errorMessage = "Failed to send invitation email: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMessage));
    }
  }*/

  @Override
  @PostMapping("/invite/{recipientEmail}")
  public ResponseEntity<Map<String, String>> invite(@PathVariable String recipientEmail) {
    if (!EmailUtil.isValidEmail(recipientEmail)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", "Invalid email address"));
    }


    if (userRepo.existsByEmail(recipientEmail)) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("error", "User already exists"));
    }

    try {
      Map<String, String> response = mailService.sendInvitationEmail(recipientEmail);
      return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of("message", "Invitation email sent successfully", "details", response.toString()));
    } catch (MailSendException e) {
      String errorMessage = "Failed to send invitation email: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", errorMessage));
    }
  }
}


package com.example.weatherService.services;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface MailService {
  Map<String, String> sendInvitationEmail(String recipientEmail);
  void forgotMail(String to, String subject, String password) throws MessagingException;

}

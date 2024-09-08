package com.example.weatherService.services;

import com.example.weatherService.repositories.UserRepo;
import com.example.weatherService.servicesImpl.MailServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {MailServiceImpl.class, UserRepo.class})
public class MailServiceTest {

  @Mock
  private JavaMailSender javaMailSender;

  @InjectMocks
  private MailServiceImpl mailService;


  @Test
  @Order(1)
  @DisplayName("Test sending email successfully")
  void testSendEmailSuccess() {
    String recipientEmail = "test@example.com";
    String subject = "Test Subject";
    String content = "This is a test email.";

    mailService.sendEmail(recipientEmail, subject, content);

    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
    verify(javaMailSender).send(captor.capture());

    SimpleMailMessage capturedMessage = captor.getValue();
    assertEquals("pekowalkowski@gmail.com", capturedMessage.getFrom());
    assertEquals(recipientEmail, capturedMessage.getTo()[0]);
    assertEquals(subject, capturedMessage.getSubject());
    assertEquals(content, capturedMessage.getText());
  }
}

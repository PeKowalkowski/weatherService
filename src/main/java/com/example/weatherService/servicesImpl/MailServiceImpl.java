package com.example.weatherService.servicesImpl;

import com.example.weatherService.config.jwtConfig.TokenService;
import com.example.weatherService.entities.User;
import com.example.weatherService.exceptions.UserAlreadyExistException;
import com.example.weatherService.repositories.UserRepo;
import com.example.weatherService.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private final UserRepo userRepo;
  private final TokenService tokenService;
  //private static final String CLIENT_URL = "http://localhost:4200";
  private final JavaMailSender javaMailSender;



  @Override
  public Map<String, String> sendInvitationEmail(String recipientEmail) {

    Optional<User> user = userRepo.findByEmail(recipientEmail);

    if (user.isPresent()) {
      throw new UserAlreadyExistException(recipientEmail);

    }
    String token = tokenService.createInvitationToken(recipientEmail);
    String subject = "Bet - rejestracja";
   // String invitationLink = CLIENT_URL + "/mail-invitation-signup?token=" + token + "&email=" + recipientEmail;
    String htmlContent = "Token : " + token;
    //String htmlContent = "Otwórz w <a href=\"" + invitationLink +"\">przeglądarce</a> lub <a href=\"http://192.168.1.38:3010/redirect?url=betmobileapp://signup/" +token + "/" + recipientEmail + "\">na urządzeniu mobilnym</a>";
    sendEmail(recipientEmail, subject, htmlContent);

    Map<String, String> response = new HashMap<>();
    response.put("status", "success");
    response.put("token", token);

    return response;
  }

  public void sendEmail(String recipientEmail, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("pekowalkowski@gmail.com");
    message.setTo(recipientEmail);
    message.setSubject(subject);
    message.setText(content);

    javaMailSender.send(message);
  }

  public void forgotMail(String to, String subject, String password) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom("pekowalkowski@gmail.com");
    helper.setTo(to);
    helper.setSubject(subject);
    String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " +
      to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
    message.setContent(htmlMsg,"text/html");
    javaMailSender.send(message);
  }


}

package com.example.weatherService.utils;

import com.example.weatherService.config.userConfig.UserConfig;
import com.example.weatherService.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

  private final UserRepo userRepo;

  public String getUserEmail(Jwt jwtToken){
    return jwtToken.getClaimAsString("sub");
  }

  public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){
    final String userEmail = getUserEmail(jwtToken);
    boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
    boolean isTokenUserSameAsDatabase = userEmail.equals(userDetails.getUsername());
    return !isTokenExpired  && isTokenUserSameAsDatabase;
  }

  private boolean getIfTokenIsExpired(Jwt jwtToken) {
    return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
  }

  public UserDetails userDetails(String emailId){
    return userRepo
      .findByEmail(emailId)
      .map(UserConfig::new)
      .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+emailId+" does not exist"));
  }
}

package com.example.weatherService.config.jwtConfig;

import com.example.weatherService.models.TokenPayload;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
/*
  private static final String JWT_SECRET = "eyJhbGciOiJIUzUxMiJ9eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcxNTQyNTc2MywiaWF0IjoxNzE1NDI1NzYzfQjg0YVDv53rOkC9iFr1I5IGFzjmTXR3sq8luhgPZstuEmKVC8SjuXCYhFBNERTdx7cdqsvE9TWmdwMB3oMQ";
*/
  @Value("${jwt.secret}")
  private String jwtSecret;
  private static final long JWT_INVITATION_EXP = 3600;
  private final JwtEncoder jwtEncoder;


  public String createInvitationToken(String recipientEmail) {


    Date now = new Date();


    Date expiration = new Date(now.getTime() + JWT_INVITATION_EXP * 1000);

    Claims claims = Jwts.claims()
      .setIssuer("pablo")
      .setIssuedAt(now)
      .setExpiration(expiration)
      .setSubject(recipientEmail)
      .build();

    String jwtToken = Jwts.builder()
      .setClaims(claims)
      .signWith(SignatureAlgorithm.HS512, jwtSecret)
      .compact();

    log.info("Generated Invitation Token for {}: {}", recipientEmail, jwtToken);
    return jwtToken;
  }


  public TokenPayload verifyMailInvitationToken(String token) {
    try {

      Jws<Claims> jwtClaims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .build().parseClaimsJws(token);

      Claims claims = jwtClaims.getBody();

      String email = claims.getSubject();

      return new TokenPayload(email);

    } catch (Exception e) {
      String errorMessage = "Invalid token: " + e.getMessage();
      throw new RuntimeException(errorMessage, e);
    }
  }

  public String generateAccessToken(Authentication authentication) {

    log.info("[JwtTokenGenerator:generateAccessToken] Token Creation Started for:{}", authentication.getName());

    String roles = getRolesOfUser(authentication);

    String permissions = getPermissionsFromRoles(roles);

    JwtClaimsSet claims = JwtClaimsSet.builder()
      .issuer("pablo")
      .issuedAt(Instant.now())
      .expiresAt(Instant.now().plus(1 , ChronoUnit.MINUTES))
      .subject(authentication.getName())
      .claim("scope", permissions)
      .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  private static String getRolesOfUser(Authentication authentication) {
    return authentication.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(" "));
  }
  private String getPermissionsFromRoles(String roles) {
    Set<String> permissions = new HashSet<>();

    if (roles.contains("ROLE_ADMIN")) {
      permissions.addAll(List.of("READ", "WRITE", "DELETE"));
    }
    if (roles.contains("ROLE_USER")) {
      permissions.add("READ");
    }

    return String.join(" ", permissions);
  }


  public String generateRefreshToken(Authentication authentication) {

    log.info("[JwtTokenGenerator:generateRefreshToken] Token Creation Started for:{}", authentication.getName());


    JwtClaimsSet claims = JwtClaimsSet.builder()
      .issuer("pablo")
      .issuedAt(Instant.now())
      .expiresAt(Instant.now().plus(15 , ChronoUnit.DAYS))
      .subject(authentication.getName())
      .claim("scope", "REFRESH_TOKEN")
      .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }


}

package com.example.weatherService.servicesImpl;

import com.example.weatherService.WeatherConstans.WeatherConstans;
import com.example.weatherService.config.jwtConfig.JwtTokenGenerator;
import com.example.weatherService.config.jwtConfig.TokenService;
import com.example.weatherService.entities.RefreshToken;
import com.example.weatherService.entities.User;
import com.example.weatherService.enums.TokenType;
import com.example.weatherService.exceptions.UserAlreadyExistException;
import com.example.weatherService.models.AuthResponseDto;
import com.example.weatherService.models.TokenPayload;
import com.example.weatherService.repositories.RefreshTokenRepo;
import com.example.weatherService.repositories.UserRepo;
import com.example.weatherService.services.AuthService;
import com.example.weatherService.utils.WeatherUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final TokenService tokenService;
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRepo refreshTokenRepo;
  private final JwtTokenGenerator jwtTokenGenerator;
  private final MailServiceImpl mailServiceImpl;


  @Override
  public boolean registerUser(String token, String password) {


    TokenPayload tokenPayload = tokenService.verifyMailInvitationToken(token);

    Optional<User> existingUser = userRepo.findByEmail(tokenPayload.getEmail());
    if (existingUser.isPresent()) {
      throw new UserAlreadyExistException(tokenPayload.getEmail());
    }

    User newUser = new User();
    newUser.setEmail(tokenPayload.getEmail());
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setCreatedAt(LocalDateTime.now());


    userRepo.save(newUser);

    return true;
  }

  @Override
  public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
    try {
      User user = userRepo.findByEmail(authentication.getName())
        .orElseThrow(() -> {
          log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
          return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
        });


      String accessToken = tokenService.generateAccessToken(authentication);
      String refreshToken = tokenService.generateRefreshToken(authentication);

      saveUserRefreshToken(user,refreshToken);

      creatRefreshTokenCookie(response,refreshToken);


      log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated", user.getFirstName());
      return AuthResponseDto.builder()
        .accessToken(accessToken)
        .accessTokenExpiry(15 * 60)
        .userName(user.getFirstName())
        .tokenType(TokenType.Bearer)
        .build();


    } catch (Exception e) {
      log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
    }
  }

  public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

    if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
      return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
    }

    final String refreshToken = authorizationHeader.substring(7);

    var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
      .filter(tokens-> !tokens.isRevoked())
      .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

    User user = refreshTokenEntity.getUser();

    Authentication authentication =  createAuthenticationObject(user);

    String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

    return  AuthResponseDto.builder()
      .accessToken(accessToken)
      .accessTokenExpiry(5 * 60)
      .userName(user.getFirstName())
      .tokenType(TokenType.Bearer)
      .build();

  }

  private void saveUserRefreshToken(User user, String refreshToken) {
    RefreshToken refreshTokenEntity = RefreshToken.builder()
      .user(user)
      .refreshToken(refreshToken)
      .revoked(false)
      .build();
    refreshTokenRepo.save(refreshTokenEntity);
  }

  private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
    response.addCookie(refreshTokenCookie);
    return refreshTokenCookie;
  }
  private static Authentication createAuthenticationObject(User user) {
    String username = user.getEmail();
    String password = user.getPassword();
    String roles = user.getRoles();

    String[] roleArray = roles.split(",");
    GrantedAuthority[] authorities = Arrays.stream(roleArray)
      .map(role -> (GrantedAuthority) role::trim)
      .toArray(GrantedAuthority[]::new);

    return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
  }

  @Override
  public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
    try {
      Optional<User> user = userRepo.findByEmail(requestMap.get("email"));
      //if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.get().getEmail()))
      //if (user.isPresent() && user.map(User2::getEmail).filter(email -> !email.isEmpty()).isPresent())
      if (user != null && user.get().getEmail() != null && !user.get().getEmail().isEmpty())
        mailServiceImpl.forgotMail(user.get().getEmail(), "Credentials by Cafe Management System", user.get().getPassword());
      return WeatherUtils.getResponseEntity("Check Your email for credentials.", HttpStatus.OK, null);
    } catch (Exception ex) {
      ex.printStackTrace();
      return WeatherUtils.getResponseEntity(WeatherConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }


  }
}

package com.example.weatherService.config;

import com.example.weatherService.config.jwtConfig.JwtAccessTokenFilter;
import com.example.weatherService.config.jwtConfig.JwtRefreshTokenFilter;
import com.example.weatherService.config.userConfig.UserManagerConfig;
import com.example.weatherService.repositories.RefreshTokenRepo;
import com.example.weatherService.utils.JwtTokenUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

  private final UserManagerConfig userManagerConfig;
  private final RSAKeyRecord rsaKeyRecord;
  private final JwtTokenUtils jwtTokenUtils;
  private final RefreshTokenRepo refreshTokenRepo;

  @Order(1)
  @Bean
  public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
    return httpSecurity
      .securityMatcher(new AntPathRequestMatcher("/sign-in/**"))
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
      .userDetailsService(userManagerConfig)
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(ex -> {
        ex.authenticationEntryPoint((request, response, authException) ->
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
      })
      .httpBasic(withDefaults())
      .build();
  }



  @Order(2)
  @Bean
  public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
    return httpSecurity
      .securityMatcher(new AntPathRequestMatcher("/api/**"))
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore((Filter) new JwtAccessTokenFilter(rsaKeyRecord, jwtTokenUtils), UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling(ex -> {
        log.error("[SecurityConfig:apiSecurityFilterChain] Exception due to :{}",ex);
        ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
        ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
      })
      .httpBasic(withDefaults())
      .build();
  }

  @Order(3)
  @Bean
  public SecurityFilterChain refreshTokenSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
    return httpSecurity
      .securityMatcher(new AntPathRequestMatcher("/refresh-token/**"))
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
      .addFilterBefore(new JwtRefreshTokenFilter(rsaKeyRecord,jwtTokenUtils,refreshTokenRepo), UsernamePasswordAuthenticationFilter.class)

      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(ex -> {
        log.error("[SecurityConfig:refreshTokenSecurityFilterChain] Exception due to :{}",ex);
        ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
        ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
      })
      .httpBasic(withDefaults())
      .build();
  }


}

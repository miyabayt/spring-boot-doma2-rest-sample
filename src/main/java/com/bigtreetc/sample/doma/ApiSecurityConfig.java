package com.bigtreetc.sample.doma;

import com.bigtreetc.sample.doma.base.web.security.ApplicationSecurityConfig;
import com.bigtreetc.sample.doma.base.web.security.authorization.JsonAccessDeniedHandler;
import com.bigtreetc.sample.doma.base.web.security.jwt.*;
import com.bigtreetc.sample.doma.base.web.security.jwt.JwtAuthenticationFilter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties({ApplicationSecurityConfig.class})
public class ApiSecurityConfig {

  @Autowired LettuceConnectionFactory lettuceConnectionFactory;

  @Autowired ApplicationSecurityConfig config;

  @Autowired UserDetailsService userDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder)
      throws Exception {
    val permittedUrls = config.getPermittedUrls().toArray(new String[0]);

    http.csrf()
        .disable()
        .authorizeRequests()
        // エラー画面は認証をかけない
        .antMatchers(permittedUrls)
        .permitAll()
        // エラー画面以外は、認証をかける
        .anyRequest()
        .authenticated()
        .and()
        .addFilter(jwtAuthenticationFilter(http, passwordEncoder))
        .addFilterAfter(jwtRefreshFilter(), JwtAuthenticationFilter.class)
        .addFilterAfter(jwtVerificationFilter(), JwtRefreshFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        .accessDeniedHandler(new JsonAccessDeniedHandler())
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public StringRedisTemplate redisTemplate() {
    val template = new StringRedisTemplate(lettuceConnectionFactory);
    template.setEnableTransactionSupport(true);
    return template;
  }

  @Bean
  public JwtRepository jwtRepository() {
    val jwtConfig = config.getJwt();
    val accessTokenConfig = jwtConfig.getAccessToken();
    val signingKey = accessTokenConfig.getSigningKey();
    val expiresIn = accessTokenConfig.getExpiredIn();
    val refreshTokenConfig = jwtConfig.getRefreshToken();
    val timeoutHours = refreshTokenConfig.getTimeoutHours();
    val repository = new JwtRepository();
    repository.setRedisTemplate(redisTemplate());
    repository.setSigningKey(signingKey);
    repository.setExpiresIn(expiresIn);
    repository.setRefreshTokenTimeoutHours(timeoutHours);
    return repository;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(
      HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
    val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
    val authenticationManager = authenticationManagerBuilder.build();
    val filter = new JwtAuthenticationFilter(authenticationManager, "/api/auth/login");
    filter.setRepository(jwtRepository());
    return filter;
  }

  @Bean
  public JwtRefreshFilter jwtRefreshFilter() {
    val filter = new JwtRefreshFilter("/api/auth/refresh");
    filter.setRepository(jwtRepository());
    return filter;
  }

  @Bean
  public JwtVerificationFilter jwtVerificationFilter() {
    val jwtConfig = config.getJwt();
    val signingKey = jwtConfig.getAccessToken().getSigningKey();
    return new JwtVerificationFilter(signingKey, "/**");
  }
}

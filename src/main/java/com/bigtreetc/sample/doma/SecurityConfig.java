package com.bigtreetc.sample.doma;

import com.bigtreetc.sample.doma.base.web.security.AppSecurityConfig;
import com.bigtreetc.sample.doma.base.web.security.CorsProperties;
import com.bigtreetc.sample.doma.base.web.security.JsonAccessDeniedHandler;
import com.bigtreetc.sample.doma.base.web.security.jwt.*;
import com.bigtreetc.sample.doma.base.web.security.jwt.JwtAuthenticationFilter;
import java.util.ArrayList;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableMethodSecurity
@Configuration
@EnableConfigurationProperties({CorsProperties.class, AppSecurityConfig.class})
@EnableWebSecurity
public class SecurityConfig {

  @Autowired LettuceConnectionFactory lettuceConnectionFactory;

  @Autowired AppSecurityConfig securityConfig;

  @Bean
  public CorsFilter corsFilter(CorsProperties corsProperties) {
    val corsConfig = new CorsConfiguration();
    corsConfig.setAllowCredentials(corsProperties.getAllowCredentials());
    corsConfig.setAllowedHeaders(corsProperties.getAllowedHeaders());
    corsConfig.setAllowedMethods(corsProperties.getAllowedMethods());
    corsConfig.setAllowedOrigins(corsProperties.getAllowedOrigins());
    corsConfig.setExposedHeaders(corsProperties.getExposedHeaders());
    corsConfig.setMaxAge(corsProperties.getMaxAge());

    val source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);
    return new CorsFilter(source);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    val permittedUrls = securityConfig.getPermittedUrls().toArray(new String[0]);
    val authenticationManager = http.getSharedObject(AuthenticationManager.class);
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize ->
                authorize.requestMatchers(permittedUrls).permitAll().anyRequest().authenticated())
        .addFilter(jwtAuthenticationFilter(authenticationManager))
        .addFilterAfter(jwtRefreshFilter(), JwtAuthenticationFilter.class)
        .addFilterAfter(jwtVerificationFilter(), JwtRefreshFilter.class)
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                    .accessDeniedHandler(new JsonAccessDeniedHandler()))
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
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
    val jwtConfig = securityConfig.getJwt();
    val accessTokenConfig = jwtConfig.getAccessToken();
    val accessTokenSigningKey = accessTokenConfig.getSigningKey();
    val accessTokenExpiresIn = accessTokenConfig.getExpiredIn();
    val refreshTokenConfig = jwtConfig.getRefreshToken();
    val refreshTokenSigningKey = refreshTokenConfig.getSigningKey();
    val timeoutMinutes = refreshTokenConfig.getTimeoutMinutes();

    val repository = new JwtRepository();
    repository.setRedisTemplate(redisTemplate());
    repository.setAccessTokenSigningKey(accessTokenSigningKey);
    repository.setAccessTokenExpiresIn(accessTokenExpiresIn);
    repository.setRefreshTokenSigningKey(refreshTokenSigningKey);
    repository.setRefreshTokenExpiresIn(timeoutMinutes * 60L * 1000);
    return repository;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(
      AuthenticationManager authenticationManager) {
    val matcher = new AntPathRequestMatcher("/api/auth/login", "POST");
    val filter = new JwtAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setRequiresAuthenticationRequestMatcher(matcher);
    filter.setRepository(jwtRepository());
    return filter;
  }

  @Bean
  public JwtRefreshFilter jwtRefreshFilter() {
    val jwtConfig = securityConfig.getJwt();
    val signingKey = jwtConfig.getRefreshToken().getSigningKey();
    val matcher = new AntPathRequestMatcher("/api/auth/refresh", "POST");
    val filter = new JwtRefreshFilter(signingKey);
    filter.setRepository(jwtRepository());
    filter.setRequiresAuthenticationRequestMatcher(matcher);
    return filter;
  }

  @Bean
  public JwtVerificationFilter jwtVerificationFilter() {
    val jwtConfig = securityConfig.getJwt();
    val signingKey = jwtConfig.getAccessToken().getSigningKey();
    val matcher = new NegatedRequestMatcher(permittedUrlsMatcher());
    val filter = new JwtVerificationFilter(signingKey);
    filter.setRequiresAuthenticationRequestMatcher(matcher);
    return filter;
  }

  private RequestMatcher permittedUrlsMatcher() {
    val permittedUrls = securityConfig.getPermittedUrls().toArray(new String[0]);
    val tempMatcherList = new ArrayList<RequestMatcher>();
    for (val url : permittedUrls) {
      tempMatcherList.add(new AntPathRequestMatcher(url));
    }
    return new OrRequestMatcher(tempMatcherList);
  }
}

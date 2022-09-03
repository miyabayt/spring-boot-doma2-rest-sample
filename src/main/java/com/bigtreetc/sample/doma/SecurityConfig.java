package com.bigtreetc.sample.doma;

import com.bigtreetc.sample.doma.base.web.security.AppSecurityConfig;
import com.bigtreetc.sample.doma.base.web.security.CorsProperties;
import com.bigtreetc.sample.doma.base.web.security.JsonAccessDeniedHandler;
import com.bigtreetc.sample.doma.base.web.security.jwt.*;
import com.bigtreetc.sample.doma.base.web.security.jwt.JwtAuthenticationFilter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableConfigurationProperties({CorsProperties.class, AppSecurityConfig.class})
public class SecurityConfig {

  @Autowired LettuceConnectionFactory lettuceConnectionFactory;

  @Autowired AppSecurityConfig securityConfig;

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter(CorsProperties corsProperties) {
    val corsConfig = new CorsConfiguration();
    corsConfig.setAllowCredentials(corsProperties.getAllowCredentials());
    corsConfig.setAllowedHeaders(corsProperties.getAllowedHeaders());
    corsConfig.setAllowedMethods(corsProperties.getAllowedMethods());
    corsConfig.setAllowedOrigins(corsProperties.getAllowedOrigins());
    corsConfig.setExposedHeaders(corsProperties.getExposedHeaders());
    corsConfig.setMaxAge(corsProperties.getMaxAge());

    val source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    val bean = new FilterRegistrationBean<>(new CorsFilter(source));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    val permittedUrls = securityConfig.getPermittedUrls().toArray(new String[0]);
    val authenticationManager = http.getSharedObject(AuthenticationManager.class);
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
        .addFilter(jwtAuthenticationFilter(authenticationManager))
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
      AuthenticationManager authenticationManager) {
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
    val jwtConfig = securityConfig.getJwt();
    val signingKey = jwtConfig.getAccessToken().getSigningKey();
    return new JwtVerificationFilter(signingKey, "/**");
  }
}

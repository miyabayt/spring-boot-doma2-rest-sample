package com.bigtreetc.sample.doma.base.web.security.jwt;

import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Getter
@Setter
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private JwtRepository repository;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (!this.requiresAuthentication(request, response)) {
      chain.doFilter(request, response);
    } else {
      super.doFilter(req, res, chain);
    }
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    LoginRequest credentials = null;
    try {
      val input = request.getInputStream();
      credentials = OBJECT_MAPPER.readValue(input, LoginRequest.class);
    } catch (IOException e) {
      // トークンが不正の場合
      throw new AuthenticationServiceException("トークンが不正です。", e);
    }

    String username = credentials.getUsername();
    String password = credentials.getPassword();

    if (username == null) {
      username = "";
    }

    if (password == null) {
      password = "";
    }

    val authRequest = new JwtAuthenticationToken(username, password, null);

    return getAuthenticationManager().authenticate(authRequest);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException {

    val username = ((User) authResult.getPrincipal()).getUsername();
    val authorities =
        authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList());

    // アクセストークン
    val token = repository.createAccessToken(username, authorities);
    response.addHeader(JwtConst.HEADER, JwtConst.TOKEN_PREFIX + token);
    response.setHeader(
        HttpHeaders.CONTENT_TYPE,
        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8).toString());
    response.setStatus(HttpStatus.OK.value());

    // リフレッシュトークン
    val refreshToken = repository.createRefreshToken(username);
    val jwtToken = new JwtObject();
    jwtToken.setAccessToken(token);
    jwtToken.setRefreshToken(refreshToken);

    val resource = new SimpleApiResponseImpl();
    resource.success(jwtToken);

    try (val writer = response.getWriter()) {
      OBJECT_MAPPER.writeValue(writer, resource);
    }
  }
}

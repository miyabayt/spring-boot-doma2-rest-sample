package com.bigtreetc.sample.doma.base.web.security.jwt;

import static com.bigtreetc.sample.doma.base.web.BaseWebConst.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bigtreetc.sample.doma.base.util.MessageUtils;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ErrorApiResponseImpl;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

@Getter
@Setter
@Slf4j
public class JwtVerificationFilter extends GenericFilterBean {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final Algorithm algorithm;

  private RequestMatcher requiresAuthenticationRequestMatcher = AnyRequestMatcher.INSTANCE;

  /**
   * コンストラクタ
   *
   * @param signingKey
   */
  public JwtVerificationFilter(String signingKey) {
    this.algorithm = Algorithm.HMAC512(signingKey);
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (!requiresAuthentication(request, response)) {
      chain.doFilter(request, response);
      return;
    }

    try {
      val tokenPayload = request.getHeader(JwtConst.HEADER);
      val accessToken = extractAccessToken(tokenPayload);
      val verifier = JWT.require(algorithm).build();
      val jwt = verifier.verify(accessToken);

      val username = jwt.getClaim(JwtConst.USERNAME).asString();
      if (username == null || username.isEmpty()) {
        String msg = String.format("username=%s", username);
        throw new JWTVerificationException(msg);
      }

      val roles = jwt.getClaim(JwtConst.ROLES).asList(String.class);
      val authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();
      val authentication = new JwtAuthenticationToken(username, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      chain.doFilter(request, response);
    } catch (Exception e) {
      log.debug("unauthorized.", e);

      // 認証エラーを返却する
      val message = MessageUtils.getMessage(UNAUTHORIZED_ERROR);
      val apiResponse = new ErrorApiResponseImpl();
      apiResponse.setMessage(message);

      response.setHeader(
          HttpHeaders.CONTENT_TYPE,
          new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8).toString());
      response.setStatus(HttpStatus.UNAUTHORIZED.value());

      try (val writer = response.getWriter()) {
        OBJECT_MAPPER.writeValue(writer, apiResponse);
      }
    }
  }

  private static String extractAccessToken(String tokenPayload) {
    try {
      return tokenPayload.substring(7);
    } catch (Exception e) {
      return tokenPayload;
    }
  }

  private boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
    val tokenPayload = request.getHeader(JwtConst.HEADER);
    if (tokenPayload == null || !tokenPayload.startsWith(JwtConst.TOKEN_PREFIX)) {
      return false;
    }
    return requiresAuthenticationRequestMatcher.matches(request);
  }
}

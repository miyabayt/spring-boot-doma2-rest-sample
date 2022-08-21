package com.bigtreetc.sample.doma.base.web.security.jwt;

import static com.bigtreetc.sample.doma.base.web.BaseWebConst.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bigtreetc.sample.doma.base.util.MessageUtils;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ErrorApiResponseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

@Getter
@Setter
@Slf4j
public class JwtVerificationFilter extends GenericFilterBean {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private String signingKey = "CCp7QvauLvIvweVt8XblRNhgkoTXBR";

  private final Algorithm algorithm;

  private RequestMatcher requiresAuthenticationRequestMatcher;

  /**
   * コンストラクタ
   *
   * @param pathPattern
   */
  public JwtVerificationFilter(String pathPattern) {
    this.algorithm = Algorithm.HMAC512(signingKey);
    this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(pathPattern));
  }

  /**
   * コンストラクタ
   *
   * @param signingKey
   * @param pathPattern
   */
  public JwtVerificationFilter(String signingKey, String pathPattern) {
    this.signingKey = signingKey;
    this.algorithm = Algorithm.HMAC512(signingKey);
    this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(pathPattern));
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
      val accessToken = tokenPayload.substring(7);
      val verifier = JWT.require(algorithm).build();
      val jwt = verifier.verify(accessToken);

      val username = jwt.getClaim(JwtConst.USERNAME).asString();
      if (username == null || "".equals(username)) {
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
      ;
      response.setStatus(HttpStatus.UNAUTHORIZED.value());

      try (val writer = response.getWriter()) {
        OBJECT_MAPPER.writeValue(writer, apiResponse);
      }
    }
  }

  protected boolean requiresAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    val tokenPayload = request.getHeader(JwtConst.HEADER);
    if (tokenPayload == null || !tokenPayload.startsWith(JwtConst.TOKEN_PREFIX)) {
      return false;
    }
    return requiresAuthenticationRequestMatcher.matches(request);
  }
}

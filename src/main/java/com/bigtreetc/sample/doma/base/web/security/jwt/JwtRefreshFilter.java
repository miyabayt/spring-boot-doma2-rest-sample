package com.bigtreetc.sample.doma.base.web.security.jwt;

import static com.bigtreetc.sample.doma.base.web.BaseWebConst.UNAUTHORIZED_ERROR;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bigtreetc.sample.doma.base.util.MessageUtils;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ErrorApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

@Getter
@Setter
@Slf4j
public class JwtRefreshFilter extends GenericFilterBean {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private JwtRepository repository;

  private RequestMatcher requiresAuthenticationRequestMatcher;

  /**
   * コンストラクタ
   *
   * @param pathPattern
   */
  public JwtRefreshFilter(String pathPattern) {
    super();
    this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(pathPattern, "POST"));
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

    Object resource = null;
    RefreshTokenRequest token = null;
    String newAccessToken = null;
    String newRefreshToken = null;
    try {
      val input = request.getInputStream();
      token = OBJECT_MAPPER.readValue(input, RefreshTokenRequest.class);

      val accessToken = token.getAccessToken();
      val refreshToken = token.getRefreshToken();
      val jwt = parseToken(accessToken);
      val username = getUsername(jwt);
      val authorities = getAuthorities(jwt);

      val valid = repository.verifyRefreshToken(username, refreshToken);
      if (!valid) {
        throw new InsufficientAuthenticationException("リフレッシュトークンが不正です。");
      }

      newAccessToken = repository.createAccessToken(username, authorities);
      newRefreshToken = repository.renewRefreshToken(username, refreshToken);

      val jwtToken = new JwtObject();
      jwtToken.setAccessToken(newAccessToken);
      jwtToken.setRefreshToken(newRefreshToken);

      val simpleResource = new SimpleApiResponseImpl();
      simpleResource.success(jwtToken);
      resource = simpleResource;
      response.setStatus(HttpStatus.OK.value());
    } catch (Exception e) {
      log.debug("cloud not refresh token.", e);

      val message = MessageUtils.getMessage(UNAUTHORIZED_ERROR);
      val apiResponse = new ErrorApiResponseImpl();
      apiResponse.setMessage(message);
      resource = apiResponse;
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    response.setHeader(
        HttpHeaders.CONTENT_TYPE,
        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8).toString());
    try (val writer = response.getWriter()) {
      OBJECT_MAPPER.writeValue(writer, resource);
    }
  }

  /**
   * トークンからユーザ名を取り出します。
   *
   * @param accessToken
   * @return
   */
  private DecodedJWT parseToken(String accessToken) {
    DecodedJWT jwt = null;
    try {
      jwt = JWT.decode(accessToken);
    } catch (Exception e) {
      throw new JWTDecodeException("アクセストークンが不正です。");
    }
    return jwt;
  }

  /**
   * ユーザ名を取り出します。
   *
   * @param jwt
   * @return
   */
  private String getUsername(DecodedJWT jwt) {
    val username = jwt.getClaim(JwtConst.USERNAME).asString();
    if (username == null || "".equals(username)) {
      String msg = String.format("username=%s", username);
      throw new JWTVerificationException(msg);
    }
    return username;
  }

  /**
   * 権限情報を取り出します。
   *
   * @param jwt
   * @return
   */
  private List<String> getAuthorities(DecodedJWT jwt) {
    return jwt.getClaim(JwtConst.ROLES).asList(String.class);
  }

  protected boolean requiresAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    return requiresAuthenticationRequestMatcher.matches(request);
  }
}

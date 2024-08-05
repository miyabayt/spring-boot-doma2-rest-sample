package com.bigtreetc.sample.doma.base.web.security.jwt;

import static com.bigtreetc.sample.doma.base.web.BaseWebConst.*;
import static com.bigtreetc.sample.doma.base.web.security.jwt.JwtConst.REFRESH_TOKEN_CACHE_KEY_PREFIX;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bigtreetc.sample.doma.base.util.EnvironmentUtils;
import com.bigtreetc.sample.doma.base.util.MessageUtils;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ErrorApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

@Getter
@Setter
@Slf4j
public class JwtRefreshFilter extends GenericFilterBean {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final Algorithm algorithm;

  private RequestMatcher requiresAuthenticationRequestMatcher = AnyRequestMatcher.INSTANCE;

  private JwtRepository repository;

  /**
   * コンストラクタ
   *
   * @param signingKey
   */
  public JwtRefreshFilter(String signingKey) {
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

    Object resource = null;
    try {
      // Cookieで送信されたリフレッシュトークン
      val refreshToken =
          getCookieValue(request, COOKIE_REFRESH_TOKEN)
              .orElseThrow(() -> new InsufficientAuthenticationException("リフレッシュトークンがNULLです。"));

      // ログインIDを取り出す
      val verifier = JWT.require(algorithm).build();
      val jwt = verifier.verify(refreshToken);
      val username = jwt.getClaim(JwtConst.USERNAME).asString();
      val authorities = jwt.getClaim(JwtConst.ROLES).asList(String.class);

      // Cookieで送信されたセッションID
      val sessionId =
          getCookieValue(request, COOKIE_SESSION_ID)
              .orElseThrow(() -> new InsufficientAuthenticationException("セッションIDがNULLです。"));
      val sessionIdKey = REFRESH_TOKEN_CACHE_KEY_PREFIX + username + ":" + sessionId;

      // リフレッシュトークンのキャッシュが存在するか確認する
      val valid = repository.verifyRefreshToken(sessionIdKey, refreshToken);
      if (!valid) {
        throw new InsufficientAuthenticationException("リフレッシュトークンが不正です。");
      }

      // 新しいアクセストークンを払い出す
      val newAccessToken = repository.createAccessToken(username, authorities);
      val jwtToken = new JwtObject();
      jwtToken.setAccessToken(newAccessToken);

      // セッションIDの有効期限を延長する
      val sessionIdCookie =
          ResponseCookie.from(COOKIE_SESSION_ID, sessionId)
              .sameSite(COOKIE_SAME_SITE_STRICT)
              .secure(!EnvironmentUtils.isLocalOrTest())
              .httpOnly(true)
              .maxAge(Duration.ofHours(1)) // 1時間
              .build();

      // リフレッシュトークンを更新する
      val newRefreshToken = repository.createRefreshToken(sessionIdKey, username, authorities);
      val refreshTokenCookie =
          ResponseCookie.from(COOKIE_REFRESH_TOKEN, newRefreshToken)
              .sameSite(COOKIE_SAME_SITE_STRICT)
              .secure(!EnvironmentUtils.isLocalOrTest())
              .httpOnly(true)
              .maxAge(Duration.ofHours(1)) // 1時間
              .build();

      // Cookieヘッダを追加する
      response.addHeader(HttpHeaders.SET_COOKIE, sessionIdCookie.toString());
      response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

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

  private Optional<String> getCookieValue(HttpServletRequest request, String name) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> Objects.equals(name, cookie.getName()))
        .map(Cookie::getValue)
        .findAny();
  }

  private static String extractAccessToken(String tokenPayload) {
    try {
      return tokenPayload.substring(7);
    } catch (Exception e) {
      return tokenPayload;
    }
  }

  private boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
    return requiresAuthenticationRequestMatcher.matches(request);
  }
}

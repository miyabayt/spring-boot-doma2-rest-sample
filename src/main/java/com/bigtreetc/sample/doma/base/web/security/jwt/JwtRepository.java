package com.bigtreetc.sample.doma.base.web.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@Transactional
@Slf4j
public class JwtRepository {

  private String signingKey;

  private long expiresIn;

  private StringRedisTemplate redisTemplate;

  private int refreshTokenTimeoutHours;

  /**
   * アクセストークンを返します。
   *
   * @param username
   * @param authorities
   * @return
   */
  public String createAccessToken(String username, List<String> authorities) {
    Date issuedAt = new Date();
    Date notBefore = new Date(issuedAt.getTime());
    Date expiresAt = new Date(issuedAt.getTime() + expiresIn);

    return JWT.create()
        .withIssuedAt(issuedAt)
        .withNotBefore(notBefore)
        .withExpiresAt(expiresAt)
        .withClaim(JwtConst.USERNAME, username)
        .withArrayClaim(JwtConst.ROLES, authorities.toArray(new String[0]))
        .sign(Algorithm.HMAC512(signingKey));
  }

  /**
   * リフレッシュトークンを返します。
   *
   * @param username
   * @return
   */
  public String createRefreshToken(String username) {
    val refreshToken = RandomStringUtils.randomAlphanumeric(256);
    redisTemplate.multi();
    redisTemplate.opsForValue().set(refreshToken, username);
    redisTemplate.expire(refreshToken, refreshTokenTimeoutHours, TimeUnit.HOURS);

    if (log.isDebugEnabled()) {
      log.debug("refresh token has stored. [username={}, refreshToken={}]", username, refreshToken);
    }

    return refreshToken;
  }

  /**
   * リフレッシュトークンを検証します。
   *
   * @param username
   * @param refreshToken
   * @return
   */
  @Transactional(readOnly = true)
  public boolean verifyRefreshToken(String username, String refreshToken) {
    val value = redisTemplate.opsForValue().get(refreshToken);
    if (!Objects.equals(username, value)) {
      log.warn("invalid refresh token. username={}", username);
      return false;
    }

    return true;
  }

  /**
   * リフレッシュトークンを再発行します。
   *
   * @param username
   * @param refreshToken
   * @return
   */
  public String renewRefreshToken(String username, String refreshToken) {
    val newRefreshToken = RandomStringUtils.randomAlphanumeric(256);
    redisTemplate.multi();
    redisTemplate.delete(refreshToken);
    redisTemplate.opsForValue().set(newRefreshToken, username);
    redisTemplate.expire(newRefreshToken, refreshTokenTimeoutHours, TimeUnit.HOURS);

    if (log.isDebugEnabled()) {
      log.debug(
          "refresh token has renewed. [username={}, refreshToken={}]", username, newRefreshToken);
    }

    return newRefreshToken;
  }

  /**
   * クレームを取り出します。
   *
   * @param accessToken
   * @param claimName
   * @return
   */
  public String getClaimAsString(String accessToken, String claimName) {
    val jwt = parseToken(accessToken);
    val claim = jwt.getClaim(claimName).asString();
    if (claim == null || claim.isEmpty()) {
      throw new JWTVerificationException("could not get claim. [name=" + claimName + "]");
    }
    return claim;
  }

  /**
   * クレームを取り出します。
   *
   * @param accessToken
   * @param claimName
   * @return
   */
  public <T> List<T> getClaimAsList(String accessToken, String claimName, Class<T> clazz) {
    val jwt = parseToken(accessToken);
    val claim = jwt.getClaim(claimName).asList(clazz);
    if (claim == null || claim.isEmpty()) {
      throw new JWTVerificationException("could not get claim. [name=" + claimName + "]");
    }
    return claim;
  }

  /**
   * リフレッシュトークンを削除します。
   *
   * @param accessToken
   * @param refreshToken
   * @return
   */
  public boolean deleteRefreshToken(String accessToken, String refreshToken) {
    redisTemplate.multi();

    val expectedUsername = redisTemplate.opsForValue().get(refreshToken);
    val actualUsername = getClaimAsString(accessToken, JwtConst.USERNAME);

    if (Objects.equals(expectedUsername, actualUsername)) {
      val success = redisTemplate.delete(refreshToken);
      return Boolean.TRUE.equals(success);
    }
    return false;
  }

  /**
   * アクセストークンをデコードします。
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
}

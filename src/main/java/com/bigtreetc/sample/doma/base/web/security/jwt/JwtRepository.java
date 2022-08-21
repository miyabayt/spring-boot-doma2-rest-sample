package com.bigtreetc.sample.doma.base.web.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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
    storeRefreshToken(refreshToken, username);
    return refreshToken;
  }

  /**
   * リフレッシュトークンを検証します。
   *
   * @param username
   * @param refreshToken
   * @return
   */
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
    redisTemplate.exec();
    return newRefreshToken;
  }

  /**
   * リフレッシュトークンを永続化します。
   *
   * @param key
   * @param username
   */
  protected void storeRefreshToken(String key, String username) {
    try {
      redisTemplate.multi();
      redisTemplate.opsForValue().set(key, username);
      redisTemplate.expire(key, refreshTokenTimeoutHours, TimeUnit.HOURS);
      redisTemplate.exec();
      log.info("refresh token has stored. [name={}, key={}]", username, key);
    } catch (Throwable e) {
      log.warn("failed to store refresh token.", e);
    }
  }
}

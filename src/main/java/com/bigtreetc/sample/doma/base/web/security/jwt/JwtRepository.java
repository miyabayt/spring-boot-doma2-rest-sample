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
import org.springframework.data.redis.core.StringRedisTemplate;

@Getter
@Setter
@Slf4j
public class JwtRepository {

  private String accessTokenSigningKey;

  private long accessTokenExpiresIn;

  private String refreshTokenSigningKey;

  private long refreshTokenExpiresIn;

  private StringRedisTemplate redisTemplate;

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
    Date expiresAt = new Date(issuedAt.getTime() + accessTokenExpiresIn);

    return JWT.create()
        .withIssuedAt(issuedAt)
        .withNotBefore(notBefore)
        .withExpiresAt(expiresAt)
        .withClaim(JwtConst.USERNAME, username)
        .withArrayClaim(JwtConst.ROLES, authorities.toArray(new String[0]))
        .sign(Algorithm.HMAC512(accessTokenSigningKey));
  }

  /**
   * リフレッシュトークンを返します。
   *
   * @param key
   * @return
   */
  public String createRefreshToken(String key, String username, List<String> authorities) {
    Date issuedAt = new Date();
    Date notBefore = new Date(issuedAt.getTime());
    Date expiresAt = new Date(issuedAt.getTime() + refreshTokenExpiresIn);

    val refreshToken =
        JWT.create()
            .withIssuedAt(issuedAt)
            .withNotBefore(notBefore)
            .withExpiresAt(expiresAt)
            .withClaim(JwtConst.USERNAME, username)
            .withArrayClaim(JwtConst.ROLES, authorities.toArray(new String[0]))
            .sign(Algorithm.HMAC512(refreshTokenSigningKey));

    redisTemplate.opsForValue().set(key, refreshToken);
    redisTemplate.expire(key, refreshTokenExpiresIn, TimeUnit.MILLISECONDS);

    if (log.isDebugEnabled()) {
      log.debug(
          "refresh token has stored. [key={}, refreshToken={}, expire={}]",
          key,
          refreshToken,
          refreshTokenExpiresIn);
    }

    return refreshToken;
  }

  /**
   * リフレッシュトークンを検証します。
   *
   * @param key
   * @param refreshToken
   * @return
   */
  public boolean verifyRefreshToken(String key, String refreshToken) {
    val value = redisTemplate.opsForValue().get(key);
    if (!Objects.equals(refreshToken, value)) {
      log.warn("invalid refresh token. key={}", key);
      return false;
    }

    return true;
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
   * @param key
   * @return
   */
  public boolean deleteRefreshToken(String key) {
    try {
      redisTemplate.delete(key);
    } catch (Exception e) {
      // ignore
    }
    return true;
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

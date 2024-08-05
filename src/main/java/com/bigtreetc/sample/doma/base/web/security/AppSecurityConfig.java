package com.bigtreetc.sample.doma.base.web.security;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.security")
public class AppSecurityConfig {

  private List<String> permittedUrls = new ArrayList<>();

  private JwtConfig jwt;

  @Setter
  @Getter
  public static class JwtConfig {
    private AccessTokenConfig accessToken;
    private RefreshTokenConfig refreshToken;
  }

  @Setter
  @Getter
  public static class AccessTokenConfig {
    private String signingKey;
    private int expiredIn = 60;
  }

  @Setter
  @Getter
  public static class RefreshTokenConfig {
    private String signingKey;
    private int timeoutMinutes = 60;
  }
}

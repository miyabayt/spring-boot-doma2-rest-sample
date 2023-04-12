package com.bigtreetc.sample.doma.base.web.security;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.security")
public class AppSecurityConfig {

  private List<String> permittedUrls = new ArrayList<>();

  // https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.advanced.nested-configuration-properties
  @NestedConfigurationProperty
  private JwtConfig jwt = new JwtConfig();

  @Setter
  @Getter
  public static class JwtConfig {

    @NestedConfigurationProperty
    private AccessTokenConfig accessToken;

    @NestedConfigurationProperty
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
    private int timeoutHours = 2;
  }
}

package com.bigtreetc.sample.doma.base.web.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequest {

  // アクセストークン
  String accessToken;

  // リフレッシュトークン
  String refreshToken;
}

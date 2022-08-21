package com.bigtreetc.sample.doma.base.web.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtObject {

  String accessToken;

  String refreshToken;
}

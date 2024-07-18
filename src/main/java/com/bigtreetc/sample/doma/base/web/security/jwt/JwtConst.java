package com.bigtreetc.sample.doma.base.web.security.jwt;

public interface JwtConst {

  String USERNAME = "username";

  String ROLES = "roles";

  String HEADER = "Authorization";

  String TOKEN_PREFIX = "Bearer ";

  String REFRESH_TOKEN_CACHE_KEY_PREFIX = "token:";
}

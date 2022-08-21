package com.bigtreetc.sample.doma.base.web.security.jwt;

import com.bigtreetc.sample.doma.base.util.MessageUtils;
import com.bigtreetc.sample.doma.base.web.BaseWebConst;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ErrorApiResponseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

/** 認証が必要なリソースに未認証でアクセスしたときの処理 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public JwtAuthenticationEntryPoint() {}

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
      throws IOException {
    if (response.isCommitted()) {
      log.info("response has already been committed.");
      return;
    }

    // デフォルトは、403 Forbidden にする。
    int httpStatus = HttpStatus.FORBIDDEN.value();
    String messageKey = BaseWebConst.ACCESS_DENIED_ERROR;

    if (e instanceof BadCredentialsException) {
      log.debug("BadCredentialsException : {}", e.getMessage());
      httpStatus = HttpStatus.UNAUTHORIZED.value();
      messageKey = BaseWebConst.UNAUTHORIZED_ERROR;
    } else if (e instanceof LockedException) {
      log.debug("LockedException : {}", e.getMessage());
      httpStatus = HttpStatus.LOCKED.value();
      messageKey = BaseWebConst.ACCOUNT_LOCKED_ERROR;
    } else if (e instanceof DisabledException) {
      log.debug("DisabledException : {}", e.getMessage());
      httpStatus = HttpStatus.UNAUTHORIZED.value();
      messageKey = BaseWebConst.ACCOUNT_DISABLED_ERROR;
    } else if (e instanceof AccountExpiredException) {
      log.debug("AccountExpiredException : {}", e.getMessage());
      httpStatus = HttpStatus.UNAUTHORIZED.value();
      messageKey = BaseWebConst.UNAUTHORIZED_ERROR;
    } else if (e instanceof CredentialsExpiredException) {
      log.debug("CredentialsExpiredException : {}", e.getMessage());
      httpStatus = HttpStatus.UNAUTHORIZED.value();
      messageKey = BaseWebConst.UNAUTHORIZED_ERROR;
    } else if (e instanceof SessionAuthenticationException) {
      log.debug("SessionAuthenticationException : {}", e.getMessage());
      httpStatus = HttpStatus.UNAUTHORIZED.value();
      messageKey = BaseWebConst.UNAUTHORIZED_ERROR;
    } else {
      log.debug("{} : {}", e.getClass().getSimpleName(), e.getMessage());
    }

    val apiResponse = new ErrorApiResponseImpl();
    apiResponse.setMessage(MessageUtils.getMessage(messageKey));
    response.setHeader(
        HttpHeaders.CONTENT_TYPE,
        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8).toString());
    response.setStatus(httpStatus);

    try (val writer = response.getWriter()) {
      OBJECT_MAPPER.writeValue(writer, apiResponse);
    }
  }
}

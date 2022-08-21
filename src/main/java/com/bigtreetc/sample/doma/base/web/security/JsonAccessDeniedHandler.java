package com.bigtreetc.sample.doma.base.web.security;

import static com.bigtreetc.sample.doma.base.web.BaseWebConst.ACCESS_DENIED_ERROR;

import com.bigtreetc.sample.doma.base.util.MessageUtils;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ErrorApiResponseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/** JSON形式で認可エラーをレスポンスするためのハンドラー */
@Slf4j
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {

    val apiResponse = new ErrorApiResponseImpl();
    apiResponse.setMessage(MessageUtils.getMessage(ACCESS_DENIED_ERROR));
    response.setHeader(
        HttpHeaders.CONTENT_TYPE,
        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8).toString());
    response.setStatus(HttpStatus.FORBIDDEN.value());

    try (val writer = response.getWriter()) {
      OBJECT_MAPPER.writeValue(writer, apiResponse);
    }
  }
}

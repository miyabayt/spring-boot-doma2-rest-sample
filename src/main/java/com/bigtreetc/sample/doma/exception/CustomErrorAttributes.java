package com.bigtreetc.sample.doma.exception;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
@Slf4j
public class CustomErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(
      WebRequest webRequest, ErrorAttributeOptions options) {
    Map<String, Object> map = super.getErrorAttributes(webRequest, options);
    map.put("success", false);
    map.remove("timestamp");
    map.remove("path");
    map.remove("error");
    map.remove("trace");
    return map;
  }
}

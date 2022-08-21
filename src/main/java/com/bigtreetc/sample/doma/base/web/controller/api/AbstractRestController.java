package com.bigtreetc.sample.doma.base.web.controller.api;

import com.bigtreetc.sample.doma.base.web.controller.api.response.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Slf4j
public class AbstractRestController {

  @Autowired protected ModelMapper modelMapper;

  @SneakyThrows
  protected ResponseEntity<Resource> toResponseEntity(Resource resource, String filename) {
    return toResponseEntity(resource, filename, false);
  }

  @SneakyThrows
  protected ResponseEntity<Resource> toResponseEntity(
      Resource resource, String filename, boolean isAttachment) {

    val responseEntity =
        ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

    if (isAttachment) {
      val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
      val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);
      responseEntity.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
    }

    return responseEntity.body(resource);
  }
}

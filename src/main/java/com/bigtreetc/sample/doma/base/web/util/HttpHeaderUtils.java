package com.bigtreetc.sample.doma.base.web.util;

import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriUtils;

/** HTTPヘッダーユーティリティ */
@Slf4j
public class HttpHeaderUtils {

  private static final String CONTENT_DISPOSITION_FORMAT =
      "%s; filename=\"%s\"; filename*=UTF-8''%s";

  /**
   * Httpヘッダービルダーを返します。
   *
   * @return
   */
  public static HttpHeaderBuilder builder() {
    return new HttpHeaderBuilder();
  }

  @Setter
  @Getter
  public static class HttpHeaderBuilder {

    String contentType;

    long contentLength;

    String contentDisposition;

    /**
     * Content-Typeヘッダーを追加します。
     *
     * @param contentType
     * @return
     */
    public HttpHeaderBuilder contentType(String contentType) {
      this.setContentType(contentType);
      return this;
    }

    /**
     * Content-Lengthヘッダーを追加します。
     *
     * @param contentLength
     * @return
     */
    public HttpHeaderBuilder contentLength(long contentLength) {
      this.setContentLength(contentLength);
      return this;
    }

    /**
     * Content-Dispositionヘッダーを追加します。
     *
     * @param filename
     * @return
     */
    public HttpHeaderBuilder contentDisposition(String filename) {
      return this.contentDisposition(filename, false);
    }

    /**
     * Content-Dispositionヘッダーを追加します。
     *
     * @param filename
     * @param isInline
     */
    public HttpHeaderBuilder contentDisposition(String filename, boolean isInline) {
      val inlineOrAttachment = (isInline) ? "inline" : "attachment";
      val contentDisposition =
          String.format(
              CONTENT_DISPOSITION_FORMAT,
              inlineOrAttachment,
              filename,
              UriUtils.encode(filename, StandardCharsets.UTF_8.name()));
      this.setContentDisposition(contentDisposition);
      return this;
    }

    /**
     * Httpヘッダーをビルドします。
     *
     * @return
     */
    public HttpHeaders build() {
      val httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.valueOf(contentType));
      httpHeaders.setContentLength(contentLength);
      return httpHeaders;
    }
  }
}

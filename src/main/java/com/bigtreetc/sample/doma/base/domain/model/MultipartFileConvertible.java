package com.bigtreetc.sample.doma.base.domain.model;

/**
 * MultipartFileインターフェースがwebモジュールに依存しているので、 <br>
 * 本インターフェースを介させることで循環参照にならないようにする。
 */
public interface MultipartFileConvertible {

  void setFilename(String filename);

  void setOriginalFilename(String originalFilename);

  void setContentType(String contentType);

  void setContent(byte[] data);
}

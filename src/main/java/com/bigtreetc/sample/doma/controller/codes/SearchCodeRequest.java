package com.bigtreetc.sample.doma.controller.codes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCodeRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  // 分類コード
  String categoryCode;

  // コード値
  String codeValue;

  // 改定番号
  Integer version;
}

package com.bigtreetc.sample.doma.controller.codecategories;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeCategoryRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  // コード分類コード
  @NotEmpty String categoryCode;

  // コード分類名
  @NotEmpty String categoryName;

  // 改定番号
  Integer version;
}

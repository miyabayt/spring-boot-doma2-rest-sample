package com.bigtreetc.sample.doma.domain.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeCategoryCriteria extends CodeCategory {

  private static final long serialVersionUID = -1;

  // 分類ID（複数指定）
  List<Long> ids;
}

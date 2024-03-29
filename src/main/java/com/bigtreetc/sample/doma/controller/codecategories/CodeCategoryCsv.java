package com.bigtreetc.sample.doma.controller.codecategories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"コードID", "コード分類コード", "コード分類名"}) // CSVのヘッダ順
@Getter
@Setter
public class CodeCategoryCsv implements Serializable {

  private static final long serialVersionUID = -1L;

  @JsonProperty("コード分類ID")
  Long id;

  @JsonProperty("コード分類コード")
  String categoryCode;

  @JsonProperty("コード分類名")
  String categoryName;
}

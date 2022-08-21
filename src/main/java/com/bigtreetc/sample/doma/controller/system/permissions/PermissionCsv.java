package com.bigtreetc.sample.doma.controller.system.permissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"権限ID", "権限名"}) // CSVのヘッダ順
@Getter
@Setter
public class PermissionCsv implements Serializable {

  private static final long serialVersionUID = -1L; // TODO

  @JsonProperty("権限ID")
  Long id;

  @JsonProperty("権限名")
  String permissionName;
}

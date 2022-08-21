package com.bigtreetc.sample.doma.base.domain.model;

import com.bigtreetc.sample.doma.base.util.ValidateUtils;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;
import org.seasar.doma.Domain;

/** 表示ステータス */
@Domain(valueType = String.class, factoryMethod = "of")
public enum DisplayStatus implements StringValue {
  非表示("0"),
  表示("1");

  private String value;

  DisplayStatus(String value) {
    this.value = value;
  }

  public static DisplayStatus of(final String value) {
    return Stream.of(values())
        .filter(v -> ValidateUtils.isEquals(v.value, value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("不正な引数が指定されました。[" + value + "]"));
  }

  public String getName() {
    return name();
  }

  @JsonValue
  public String getValue() {
    return this.value;
  }
}

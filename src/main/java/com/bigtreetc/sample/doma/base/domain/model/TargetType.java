package com.bigtreetc.sample.doma.base.domain.model;

import com.bigtreetc.sample.doma.base.util.ValidateUtils;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;
import org.seasar.doma.Domain;

/** 対象区分 */
@Domain(valueType = String.class, factoryMethod = "of")
public enum TargetType implements StringValue {
  非対象("0"),
  対象("1");

  private String value;

  TargetType(String value) {
    this.value = value;
  }

  public static TargetType of(final String value) {
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

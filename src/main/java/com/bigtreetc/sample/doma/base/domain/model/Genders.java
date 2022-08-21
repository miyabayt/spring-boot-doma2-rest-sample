package com.bigtreetc.sample.doma.base.domain.model;

import com.bigtreetc.sample.doma.base.util.ValidateUtils;
import java.util.stream.Stream;
import org.seasar.doma.Domain;

/** 性別 */
@Domain(valueType = String.class, factoryMethod = "of")
public enum Genders implements StringValue {
  男性("1"),
  女性("2"),
  不明("9");

  private String value;

  Genders(String value) {
    this.value = value;
  }

  public static Genders of(final String value) {
    return Stream.of(values())
        .filter(v -> ValidateUtils.isEquals(v.value, value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("不正な引数が指定されました。[" + value + "]"));
  }

  public String getName() {
    return name();
  }

  public String getValue() {
    return this.value;
  }
}

package com.bigtreetc.sample.doma.base.web.controller;

import com.bigtreetc.sample.doma.base.domain.model.StringValue;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/** 文字列の入力値をEnum型に変換する */
@SuppressWarnings({"rawtypes", "unchecked"})
public class StringValueEnumConverterFactory implements ConverterFactory<String, Enum> {

  @Override
  public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
    return source -> {
      if (StringValue.class.isAssignableFrom(targetType)) {
        return Stream.of(targetType.getEnumConstants())
            .filter(e -> ((StringValue) e).getValue().equals(source))
            .findFirst()
            .orElse(null);
      }
      return (T) Enum.valueOf(targetType, source.trim());
    };
  }
}

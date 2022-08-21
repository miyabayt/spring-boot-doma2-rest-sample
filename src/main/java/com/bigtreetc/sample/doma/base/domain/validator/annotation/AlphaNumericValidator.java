package com.bigtreetc.sample.doma.base.domain.validator.annotation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/** 入力チェック（半角英数字） */
public class AlphaNumericValidator implements ConstraintValidator<AlphaNumeric, String> {

  private static final Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");

  @Override
  public void initialize(AlphaNumeric AlphaNumeric) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value) || p.matcher(value).matches();
  }
}

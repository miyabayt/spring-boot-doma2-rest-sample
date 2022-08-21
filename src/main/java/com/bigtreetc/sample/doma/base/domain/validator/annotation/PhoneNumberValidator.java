package com.bigtreetc.sample.doma.base.domain.validator.annotation;

import com.bigtreetc.sample.doma.base.util.ValidateUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/** 入力チェック（電話番号） */
@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private Pattern pattern;

  @Override
  public void initialize(PhoneNumber phoneNumber) {
    try {
      pattern = Pattern.compile(phoneNumber.regexp());
    } catch (PatternSyntaxException e) {
      log.error("invalid regular expression.", e);
      throw e;
    }
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean isValid = false;

    if (ValidateUtils.isEmpty(value)) {
      isValid = true;
    } else {
      Matcher m = pattern.matcher(value);

      if (m.matches()) {
        isValid = true;
      }
    }

    return isValid;
  }
}

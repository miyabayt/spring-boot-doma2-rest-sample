package com.bigtreetc.sample.doma.controller.codes;

import com.bigtreetc.sample.doma.base.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** コード登録 入力チェック */
@Component
public class CodeRequestValidator extends AbstractValidator<CodeRequest> {

  @Override
  protected void doValidate(CodeRequest request, Errors errors) {}
}

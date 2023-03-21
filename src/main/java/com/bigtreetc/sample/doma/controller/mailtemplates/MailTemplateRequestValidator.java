package com.bigtreetc.sample.doma.controller.mailtemplates;

import com.bigtreetc.sample.doma.base.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** メールテンプレート登録 入力チェック */
@Component
public class MailTemplateRequestValidator extends AbstractValidator<MailTemplateRequest> {

  @Override
  protected void doValidate(MailTemplateRequest request, Errors errors) {}
}

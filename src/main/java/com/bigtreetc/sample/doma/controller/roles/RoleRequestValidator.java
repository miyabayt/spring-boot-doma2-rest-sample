package com.bigtreetc.sample.doma.controller.roles;

import com.bigtreetc.sample.doma.base.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** ロール登録 入力チェック */
@Component
public class RoleRequestValidator extends AbstractValidator<RoleRequest> {

  @Override
  protected void doValidate(RoleRequest request, Errors errors) {}
}

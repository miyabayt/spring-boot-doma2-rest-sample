package com.bigtreetc.sample.doma.controller.staffs;

import static com.bigtreetc.sample.doma.base.util.ValidateUtils.isNotEquals;

import com.bigtreetc.sample.doma.base.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** 担当者登録 入力チェック */
@Component
public class StaffRequestValidator extends AbstractValidator<StaffRequest> {

  @Override
  protected void doValidate(StaffRequest request, Errors errors) {
    // 確認用パスワードと突き合わせる
    if (isNotEquals(request.getPassword(), request.getPasswordConfirm())) {
      errors.rejectValue("password", "staffs.unmatchPassword");
      errors.rejectValue("passwordConfirm", "staffs.unmatchPassword");
    }
  }
}

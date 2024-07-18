package com.bigtreetc.sample.doma.base.exception;

import org.springframework.validation.Errors;

/** バリデーションエラー */
public class ValidationErrorException extends RuntimeException {

  private static final long serialVersionUID = -1L;

  private transient Errors errors;

  public ValidationErrorException(Errors errors) {
    super();
    this.errors = errors;
  }

  public Errors getErrors() {
    return this.errors;
  }
}

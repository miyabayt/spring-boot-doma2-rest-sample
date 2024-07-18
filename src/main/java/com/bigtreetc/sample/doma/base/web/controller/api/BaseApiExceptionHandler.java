package com.bigtreetc.sample.doma.base.web.controller.api;

import static com.bigtreetc.sample.doma.base.web.BaseWebConst.*;

import com.bigtreetc.sample.doma.base.exception.DataInUseErrorException;
import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.util.MessageUtils;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ErrorApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.FieldErrorDto;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** API用の例外ハンドラー */
@Slf4j
public class BaseApiExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * 入力チェックエラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(ValidationErrorException.class)
  public ResponseEntity<Object> handleValidationErrorException(Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.BAD_REQUEST;
    val fieldErrorContexts = new ArrayList<FieldErrorDto>();

    if (ex instanceof ValidationErrorException vee) {
      val errors = vee.getErrors();
      if (errors != null) {
        val fieldErrors = errors.getFieldErrors();
        for (val fieldError : fieldErrors) {
          val fieldName = fieldError.getField();
          val rejectedValue = fieldError.getRejectedValue();

          val fieldErrorResource = new FieldErrorDto();
          fieldErrorResource.setFieldName(fieldName);
          fieldErrorResource.setRejectedValue(rejectedValue);

          val errorMessage = MessageUtils.getMessage(fieldError);
          fieldErrorResource.setErrorMessage(errorMessage);
          fieldErrorContexts.add(fieldErrorResource);
        }
      }
    }

    val locale = request.getLocale();
    val message = MessageUtils.getMessage(VALIDATION_ERROR, null, "validation error", locale);

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);
    response.setFieldErrors(fieldErrorContexts);

    return new ResponseEntity<>(response, headers, status);
  }

  /**
   * データ不存在エラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(NoDataFoundException.class)
  public ResponseEntity<Object> handleNoDataFoundException(Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.NOT_FOUND;
    val message =
        MessageUtils.getMessage(NO_DATA_FOUND_ERROR, null, "no data found", request.getLocale());

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, status);
  }

  /**
   * ファイル不存在エラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<Object> handleFileNotFoundException(Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.NOT_FOUND;
    val message =
        MessageUtils.getMessage(FILE_NOT_FOUND_ERROR, null, "file not found", request.getLocale());

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, status);
  }

  /**
   * 使用中エラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(DataInUseErrorException.class)
  public ResponseEntity<Object> handleInUseErrorException(Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.NOT_ACCEPTABLE;
    val message =
        MessageUtils.getMessage(DATA_IN_USE_ERROR, null, "data in-use", request.getLocale());

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, status);
  }

  /**
   * 認証エラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.UNAUTHORIZED;
    val message = MessageUtils.getMessage(UNAUTHORIZED_ERROR);

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, status);
  }

  /**
   * 不正アクセスエラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.FORBIDDEN;
    val message =
        MessageUtils.getMessage(ACCESS_DENIED_ERROR, null, "access denied", request.getLocale());

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, status);
  }

  /**
   * キー重複エラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<Object> handleDuplicateKeyException(Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.UNPROCESSABLE_ENTITY;
    val message = MessageUtils.getMessage(DUPLICATE_KEY_ERROR);

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, status);
  }

  /**
   * 楽観的排他制御エラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler({OptimisticLockingFailureException.class})
  public ResponseEntity<Object> handleOptimisticLockingFailureException(
      Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.CONFLICT;
    val message =
        MessageUtils.getMessage(
            OPTIMISTIC_LOCKING_FAILURE_ERROR, null, "optimistic lock error", request.getLocale());

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, status);
  }

  /**
   * 予期せぬ例外のハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnexpectedException(Exception ex, WebRequest request) {
    return handleExceptionInternal(ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      @Nullable Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {
    log.error("unexpected error has occurred.", ex);

    val locale = request.getLocale();
    val message = MessageUtils.getMessage(UNEXPECTED_ERROR, locale);

    val response = new ErrorApiResponseImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, statusCode);
  }
}

package com.bigtreetc.sample.doma.base.web;

/** 定数定義 */
public interface BaseWebConst {

  /** ---- Message ---- */
  String GLOBAL_MESSAGE = "GlobalMessage";

  String VALIDATION_ERROR = "ValidationError";

  String DUPLICATE_KEY_ERROR = "DuplicateKeyError";

  String OPTIMISTIC_LOCKING_FAILURE_ERROR = "OptimisticLockingFailureError";

  String CLAIM_LOCK_FAILED_ERROR = "ClaimLockFailedError";

  String DOUBLE_SUBMIT_ERROR = "DoubleSubmitError";

  String FILE_NOT_FOUND_ERROR = "FileNotFoundError";

  String NO_DATA_FOUND_ERROR = "NoDataFoundError";

  String DATA_IN_USE_ERROR = "DataInUseError";

  String UNAUTHORIZED_ERROR = "UnauthorizedError";

  String ACCESS_DENIED_ERROR = "AccessDeniedError";

  String ACCOUNT_LOCKED_ERROR = "AccountLockedError";

  String ACCOUNT_DISABLED_ERROR = "AccountDisabledError";

  String UNEXPECTED_ERROR = "UnexpectedError";

  String MESSAGE_DELETED = "Deleted";

  String MESSAGE_SUCCESS = "Success";

  /** ---- URLs ---- */
  String API_BASE_URL = "/api/**";

  /** ---- Security ---- */
  String PERMIT_ALL = "permitAll";
}

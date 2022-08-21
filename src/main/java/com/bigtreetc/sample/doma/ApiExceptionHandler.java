package com.bigtreetc.sample.doma;

import com.bigtreetc.sample.doma.base.web.controller.api.BaseApiExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** API用の例外ハンドラー */
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends BaseApiExceptionHandler {}

package com.bigtreetc.sample.doma.base.web.aop;

import static com.bigtreetc.sample.doma.base.util.ValidateUtils.isEmpty;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/** 処理時間をログに出力する */
@Slf4j
public class ElapsedMillisLoggingInterceptor implements HandlerInterceptor {

  private static final ThreadLocal<Long> startTimeHolder = new InheritableThreadLocal<>();

  private static final ThreadLocal<String> requestUriHolder = new InheritableThreadLocal<>();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // コントローラーの動作前
    if (handler instanceof ResourceHttpRequestHandler) {
      return true;
    }

    // 元のRequestURIを記録する
    val requestURI = getRequestURI(request);
    requestUriHolder.set(requestURI);

    // 現在時刻を記録
    val beforeNanoSec = System.nanoTime();
    startTimeHolder.set(beforeNanoSec);

    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // 処理完了後

    val beforeNanoSec = startTimeHolder.get();
    if (beforeNanoSec == null) {
      return;
    }

    val elapsedNanoSec = System.nanoTime() - beforeNanoSec;
    val elapsedMilliSec = NANOSECONDS.toMillis(elapsedNanoSec);
    val requestUri = requestUriHolder.get();
    log.info(
        "path={}, method={}, status={}, Elapsed {}ms.",
        requestUri,
        request.getMethod(),
        response.getStatus(),
        elapsedMilliSec);

    // 破棄する
    startTimeHolder.remove();
  }

  private String getRequestURI(HttpServletRequest request) {
    String requestURI = null;
    try {
      requestURI = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
    } catch (Exception e) {
      // ignore
    }

    if (isEmpty(requestURI)) {
      requestURI = request.getRequestURI();
    }

    return requestURI;
  }
}

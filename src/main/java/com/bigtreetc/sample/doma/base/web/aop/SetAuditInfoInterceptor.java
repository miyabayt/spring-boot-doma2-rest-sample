package com.bigtreetc.sample.doma.base.web.aop;

import com.bigtreetc.sample.doma.base.domain.dao.AuditInfoHolder;
import com.bigtreetc.sample.doma.base.util.DateUtils;
import com.bigtreetc.sample.doma.base.web.util.WebSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/** ログインユーザを監査情報ホルダーに設定する */
@Slf4j
public class SetAuditInfoInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // コントローラーの動作前
    if (handler instanceof ResourceHttpRequestHandler) {
      return true;
    }

    val now = DateUtils.now();

    // 未ログインの場合は、ゲスト扱いにする
    AuditInfoHolder.set("Unknown", now);

    // ログインユーザが存在する場合
    WebSecurityUtils.getPrincipal()
        .ifPresent(
            username -> {
              // 監査情報を設定する
              AuditInfoHolder.set(username, now);
            });

    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    // コントローラーの動作後

    // 監査情報をクリアする
    AuditInfoHolder.clear();
  }
}

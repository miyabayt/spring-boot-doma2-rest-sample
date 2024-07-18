package com.bigtreetc.sample.doma.controller.auth;

import static com.bigtreetc.sample.doma.base.web.security.jwt.JwtConst.REFRESH_TOKEN_CACHE_KEY_PREFIX;

import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.security.jwt.JwtRepository;
import com.bigtreetc.sample.doma.base.web.util.WebSecurityUtils;
import com.bigtreetc.sample.doma.domain.repository.StaffRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "認証")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/auth")
@Slf4j
public class AuthController {

  @NonNull final JwtRepository jwtRepository;

  @NonNull final StaffRepository staffRepository;

  @NonNull final ModelMapper modelMapper;

  /**
   * ログインユーザを取得します。
   *
   * @return
   */
  @GetMapping("/me")
  public ApiResponse get() {
    // ログインユーザを取得する
    val staffId =
        WebSecurityUtils.getPrincipal()
            .orElseThrow(() -> new AuthenticationServiceException("認証エラー"));

    val staff = staffRepository.findById(Long.valueOf(staffId));

    val response = new SimpleApiResponseImpl();
    response.success(staff);

    return response;
  }

  /**
   * ログアウトします。
   *
   * @return
   */
  @PostMapping("/logout")
  public ApiResponse logout(@CookieValue(name = "SESSION", required = false) String sessionId) {
    // ログインユーザを取得する
    val accountId =
        WebSecurityUtils.getPrincipal()
            .orElseThrow(() -> new AuthenticationServiceException("認証エラー"));

    if (sessionId != null) {
      val sessionIdKey = REFRESH_TOKEN_CACHE_KEY_PREFIX + ":" + accountId + ":" + sessionId;
      jwtRepository.deleteRefreshToken(sessionIdKey);

      val response = new SimpleApiResponseImpl();
      response.success();
      return response;
    } else {
      val response = new SimpleApiResponseImpl();
      response.setMessage("セッションIDを指定してください");
      return response;
    }
  }
}

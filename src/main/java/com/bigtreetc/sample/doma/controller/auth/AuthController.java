package com.bigtreetc.sample.doma.controller.auth;

import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.security.jwt.JwtRepository;
import com.bigtreetc.sample.doma.base.web.security.jwt.RefreshTokenRequest;
import com.bigtreetc.sample.doma.base.web.util.WebSecurityUtils;
import com.bigtreetc.sample.doma.domain.repository.StaffRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "認証")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
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
   * @param request
   * @return
   */
  @PostMapping("/logout")
  public ApiResponse logout(@RequestBody RefreshTokenRequest request) {
    val accessToken = request.getAccessToken();
    val refreshToken = request.getRefreshToken();
    val success = jwtRepository.deleteRefreshToken(accessToken, refreshToken);

    val response = new SimpleApiResponseImpl();
    response.setSuccess(success);

    return response;
  }
}

package com.bigtreetc.sample.doma.controller.users;

import static com.bigtreetc.sample.doma.base.util.TypeUtils.toListType;
import static com.bigtreetc.sample.doma.base.util.ValidateUtils.isNotEmpty;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.util.CsvUtils;
import com.bigtreetc.sample.doma.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.doma.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.CountApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.PageableApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.domain.model.User;
import com.bigtreetc.sample.doma.domain.model.UserCriteria;
import com.bigtreetc.sample.doma.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ユーザ")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserController extends AbstractRestController {

  @NonNull final UserRequestValidator userRequestValidator;

  @NonNull final UserService userService;

  @NonNull final PasswordEncoder passwordEncoder;

  @InitBinder
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(userRequestValidator);
  }

  /**
   * ユーザを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "ユーザ登録", description = "ユーザを登録します。")
  @PreAuthorize("hasAuthority('user:save')")
  @PostMapping("/user")
  public ApiResponse create(@Validated @RequestBody UserRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val user = modelMapper.map(request, User.class);
    val password = request.getPassword();

    // パスワードをハッシュ化する
    user.setPassword(passwordEncoder.encode(password));

    // 1件登録する
    val data = userService.create(user);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ユーザを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ユーザ一括登録", description = "ユーザを一括登録します。")
  @PreAuthorize("hasAuthority('user:save')")
  @PostMapping(value = "/users")
  public ApiResponse createAll(
      @Validated @RequestBody Requests<UserRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val users = requests.stream().map(f -> modelMapper.map(f, User.class)).toList();

    // 一括登録する
    val created = userService.createAll(users);

    val response = new CountApiResponseImpl();
    response.success(created);

    return response;
  }

  /**
   * ユーザを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "ユーザ検索", description = "ユーザを検索します。")
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping("/users")
  public ApiResponse findAll(
      @ModelAttribute SearchUserRequest request, @Parameter(hidden = true) Pageable pageable) {
    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, UserCriteria.class);

    // 10件で区切って取得する
    val data = userService.findAll(criteria, pageable);

    val response = new PageableApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ユーザを取得します。
   *
   * @param userId
   * @return
   */
  @Operation(summary = "ユーザ取得", description = "ユーザを取得します。")
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping("/user/{userId}")
  public ApiResponse findOne(@PathVariable Long userId) {
    // 1件取得する
    val data = userService.findById(userId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ユーザを更新します。
   *
   * @param userId
   * @param request
   * @return
   */
  @Operation(summary = "ユーザ更新", description = "ユーザを更新します。")
  @PreAuthorize("hasAuthority('user:save')")
  @PutMapping("/user/{userId}")
  public ApiResponse update(
      @PathVariable Long userId, @Validated @RequestBody UserRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val user = modelMapper.map(request, User.class);
    val password = user.getPassword();
    if (isNotEmpty(password)) {
      val encodedPassword = passwordEncoder.encode(password);
      user.setPassword(encodedPassword);
    }

    // 1件更新する
    user.setId(userId);
    val data = userService.update(user);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ユーザを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ユーザ一括更新", description = "ユーザを一括更新します。")
  @PreAuthorize("hasAuthority('user:save')")
  @PutMapping(value = "/users")
  public ApiResponse updateAll(
      @Validated @RequestBody Requests<UserRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val users = requests.stream().map(f -> modelMapper.map(f, User.class)).collect(toList());

    // 一括更新する
    val updated = userService.updateAll(users);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * ユーザを削除します。
   *
   * @param userId
   * @return
   */
  @Operation(summary = "ユーザ削除", description = "ユーザを削除します。")
  @PreAuthorize("hasAuthority('user:save')")
  @DeleteMapping("/user/{userId}")
  public ApiResponse delete(@PathVariable Long userId) {
    // 1件取得する
    val data = userService.delete(userId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ユーザを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ユーザ一括削除", description = "ユーザを一括削除します。")
  @PreAuthorize("hasAuthority('user:save')")
  @DeleteMapping(value = "/users")
  public ApiResponse deleteAll(
      @Validated @RequestBody Requests<DeleteUserRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val users = requests.stream().map(f -> modelMapper.map(f, User.class)).collect(toList());

    // 一括削除する
    val deleted = userService.deleteAll(users);

    val response = new CountApiResponseImpl();
    response.success(deleted);

    return response;
  }

  /**
   * CSV出力
   *
   * @param filename
   * @return
   */
  @Operation(summary = "ユーザCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping("/users/export/{filename:.+\\.csv}")
  public ResponseEntity<Resource> downloadCsv(@PathVariable String filename) throws Exception {
    val users = userService.findAll(new UserCriteria(), Pageable.unpaged());

    // 詰め替える
    List<UserCsv> csvList = modelMapper.map(users.getContent(), toListType(UserCsv.class));

    val outputStream = CsvUtils.writeCsv(UserCsv.class, csvList);
    val resource = new ByteArrayResource(outputStream.toByteArray());
    return toResponseEntity(resource, filename, true);
  }
}

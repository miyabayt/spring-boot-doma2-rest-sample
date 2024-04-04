package com.bigtreetc.sample.doma.controller.roles;

import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.doma.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.CountApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.PageableApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.domain.model.Role;
import com.bigtreetc.sample.doma.domain.model.RoleCriteria;
import com.bigtreetc.sample.doma.domain.model.RolePermission;
import com.bigtreetc.sample.doma.domain.service.PermissionService;
import com.bigtreetc.sample.doma.domain.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ロールマスタ")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RoleController extends AbstractRestController {

  @NonNull final RoleRequestValidator roleRequestValidator;

  @NonNull final RoleService roleService;

  @NonNull final PermissionService permissionService;

  @InitBinder
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(roleRequestValidator);
  }

  /**
   * ロールマスタを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "ロールマスタ登録", description = "ロールマスタを登録します。")
  @PreAuthorize("hasAuthority('role:save')")
  @PostMapping("/role")
  public ApiResponse create(@Validated @RequestBody RoleRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val role = modelMapper.map(request, Role.class);
    val entrySet = request.getPermissions().entrySet();
    for (val entry : entrySet) {
      val rp = new RolePermission();
      rp.setRoleCode(request.getRoleCode());
      rp.setPermissionCode(entry.getKey());
      rp.setIsEnabled(Boolean.TRUE.equals(entry.getValue()));
      role.getRolePermissions().add(rp);
    }

    // 1件登録する
    val data = roleService.create(role);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ロールマスタを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ロールマスタ一括登録", description = "ロールマスタを一括登録します。")
  @PreAuthorize("hasAuthority('role:save')")
  @PostMapping(value = "/roles")
  public ApiResponse createAll(
      @Validated @RequestBody Requests<RoleRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val roles = requests.stream().map(f -> modelMapper.map(f, Role.class)).toList();

    // 一括登録する
    val created = roleService.createAll(roles);

    val response = new CountApiResponseImpl();
    response.success(created);

    return response;
  }

  /**
   * ロールマスタを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "ロールマスタ検索", description = "ロールマスタを検索します。")
  @PreAuthorize("hasAuthority('role:read')")
  @GetMapping("/roles")
  public ApiResponse search(
      @ModelAttribute SearchRoleRequest request, @Parameter(hidden = true) Pageable pageable) {
    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, RoleCriteria.class);

    // 10件で区切って取得する
    val data = roleService.findAll(criteria, pageable);

    val response = new PageableApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ロールマスタを検索します。（POST版）
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "ロールマスタ検索", description = "ロールマスタを検索します。")
  @PreAuthorize("hasAuthority('role:read')")
  @PostMapping("/roles/search")
  public ApiResponse searchByPost(
      @RequestBody SearchRoleRequest request, @Parameter(hidden = true) Pageable pageable) {
    return search(request, pageable);
  }

  /**
   * ロールマスタを取得します。
   *
   * @param roleId
   * @return
   */
  @Operation(summary = "ロールマスタ取得", description = "ロールマスタを取得します。")
  @PreAuthorize("hasAuthority('role:read')")
  @GetMapping("/role/{roleId}")
  public ApiResponse findOne(@PathVariable Long roleId) {
    // 1件取得する
    val data = roleService.findById(roleId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ロールマスタを更新します。
   *
   * @param roleId
   * @param request
   * @return
   */
  @Operation(summary = "ロールマスタ更新", description = "ロールマスタを更新します。")
  @PreAuthorize("hasAuthority('role:save')")
  @PutMapping("/role/{roleId}")
  public ApiResponse update(
      @PathVariable Long roleId, @Validated @RequestBody RoleRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val role = modelMapper.map(request, Role.class);

    // 1件更新する
    role.setId(roleId);
    val entrySet = request.getPermissions().entrySet();
    for (val entry : entrySet) {
      val rp = new RolePermission();
      rp.setRoleCode(request.getRoleCode());
      rp.setPermissionCode(entry.getKey());
      rp.setIsEnabled(Boolean.TRUE.equals(entry.getValue()));
      role.getRolePermissions().add(rp);
    }

    val data = roleService.update(role);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ロールマスタを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ロールマスタ一括更新", description = "ロールマスタを一括更新します。")
  @PreAuthorize("hasAuthority('role:save')")
  @PutMapping(value = "/roles")
  public ApiResponse update(@Validated @RequestBody Requests<RoleRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val roles = requests.stream().map(f -> modelMapper.map(f, Role.class)).toList();

    // 一括更新する
    val updated = roleService.updateAll(roles);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * ロールマスタを削除します。
   *
   * @param roleId
   * @return
   */
  @Operation(summary = "ロールマスタ削除", description = "ロールマスタを削除します。")
  @PreAuthorize("hasAuthority('role:save')")
  @DeleteMapping("/role/{roleId}")
  public ApiResponse delete(@PathVariable Long roleId) {
    // 1件取得する
    val data = roleService.delete(roleId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * ロールマスタを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ロールマスタ一括削除", description = "ロールマスタを一括削除します。")
  @PreAuthorize("hasAuthority('role:save')")
  @DeleteMapping(value = "/roles")
  public ApiResponse deleteAll(
      @Validated @RequestBody Requests<DeleteRoleRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val roles = requests.stream().map(f -> modelMapper.map(f, Role.class)).toList();

    // 一括削除する
    val deleted = roleService.deleteAll(roles);

    val response = new CountApiResponseImpl();
    response.success(deleted);

    return response;
  }

  /**
   * CSV出力
   *
   * @param filename
   * @param request
   * @param response
   * @return
   */
  @Operation(summary = "ロールマスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('role:read')")
  @GetMapping("/roles/export/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute SearchRoleRequest request,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(request, RoleCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      roleService.writeToOutputStream(outputStream, criteria, RoleCsv.class);
    }
  }

  /**
   * CSV出力（POST版）
   *
   * @param request
   * @param response
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "ロールマスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('role:read')")
  @PostMapping("/roles/export/{filename:.+\\.csv}")
  public void searchByPost(
      @PathVariable String filename,
      @RequestBody SearchRoleRequest request,
      HttpServletResponse response)
      throws IOException {
    downloadCsv(filename, request, response);
  }
}

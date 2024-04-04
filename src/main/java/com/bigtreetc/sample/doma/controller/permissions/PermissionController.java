package com.bigtreetc.sample.doma.controller.permissions;

import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.doma.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.CountApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.PageableApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.domain.model.Permission;
import com.bigtreetc.sample.doma.domain.model.PermissionCriteria;
import com.bigtreetc.sample.doma.domain.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

@Tag(name = "権限マスタ")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PermissionController extends AbstractRestController {

  @NonNull final PermissionRequestValidator permissionRequestValidator;

  @NonNull final PermissionService permissionService;

  @ModelAttribute
  public PermissionRequest permissionRequest() {
    return new PermissionRequest();
  }

  @ModelAttribute
  public SearchPermissionRequest searchPermissionRequest() {
    return new SearchPermissionRequest();
  }

  @InitBinder
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(permissionRequestValidator);
  }

  /**
   * 権限マスタを登録します。
   *
   * @param request
   * @param errors
   * @return
   */
  @Operation(summary = "権限マスタ登録", description = "権限マスタを登録します。")
  @PreAuthorize("hasAuthority('permission:save')")
  @PostMapping(value = "/permission")
  public ApiResponse create(@Validated @RequestBody PermissionRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val permission = modelMapper.map(request, Permission.class);

    // 1件登録する
    val data = permissionService.create(permission);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 権限マスタを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "権限マスタ一括登録", description = "権限マスタを一括登録します。")
  @PreAuthorize("hasAuthority('permission:save')")
  @PostMapping(value = "/permissions")
  public ApiResponse createAll(
      @Validated @RequestBody Requests<PermissionRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val permissions = requests.stream().map(f -> modelMapper.map(f, Permission.class)).toList();

    // 一括登録する
    val created = permissionService.createAll(permissions);

    val response = new CountApiResponseImpl();
    response.success(created);

    return response;
  }

  /**
   * 権限マスタを検索します。
   *
   * @param request
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "権限マスタ検索", description = "権限マスタを検索します。")
  @PreAuthorize("hasAuthority('permission:read')")
  @GetMapping(value = "/permissions")
  public ApiResponse search(
      @ModelAttribute SearchPermissionRequest request,
      @Parameter(hidden = true) Pageable pageable) {
    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, PermissionCriteria.class);

    // 10件で区切って取得する
    val data = permissionService.findAll(criteria, pageable);

    val response = new PageableApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 権限マスタを検索します。（POST版）
   *
   * @param request
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "権限マスタ検索", description = "権限マスタを検索します。")
  @PreAuthorize("hasAuthority('permission:read')")
  @PostMapping(value = "/permissions/search")
  public ApiResponse searchByPost(
      @RequestBody SearchPermissionRequest request, @Parameter(hidden = true) Pageable pageable) {
    return search(request, pageable);
  }

  /**
   * 権限マスタを取得します。
   *
   * @param permissionId
   * @return
   */
  @Operation(summary = "権限マスタ取得", description = "権限マスタを取得します。")
  @PreAuthorize("hasAuthority('permission:read')")
  @GetMapping("/permission/{id}")
  public ApiResponse findOne(@PathVariable Long permissionId) {
    // 1件取得する
    val data = permissionService.findById(permissionId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 権限マスタを更新します。
   *
   * @param request
   * @param errors
   * @return
   */
  @Operation(summary = "権限マスタ更新", description = "権限マスタを更新します。")
  @PreAuthorize("hasAuthority('permission:save')")
  @PutMapping("/permission/{id}")
  public ApiResponse update(
      @PathVariable("id") Long id,
      @Validated @RequestBody PermissionRequest request,
      Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val permission = modelMapper.map(request, Permission.class);

    // 1件更新する
    permission.setId(id);
    val data = permissionService.update(permission);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 権限マスタを一括更新します。
   *
   * @param requests
   * @param errors
   * @return
   */
  @Operation(summary = "権限マスタ一括更新", description = "権限マスタを一括更新します。")
  @PreAuthorize("hasAuthority('permission:save')")
  @PutMapping("/permission")
  public ApiResponse update(
      @Validated @RequestBody List<PermissionRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val permissions = requests.stream().map(f -> modelMapper.map(f, Permission.class)).toList();

    // 更新する
    val updated = permissionService.updateAll(permissions);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * 権限マスタを削除します。
   *
   * @param id
   * @return
   */
  @Operation(summary = "権限マスタ削除", description = "権限マスタを削除します。")
  @PreAuthorize("hasAuthority('permission:save')")
  @DeleteMapping("/permission/{id}")
  public ApiResponse delete(@PathVariable("id") Long id) {
    // 1件取得する
    val data = permissionService.delete(id);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 権限マスタを一括削除します。
   *
   * @param requests
   * @param errors
   * @return
   */
  @Operation(summary = "権限マスタ一括削除", description = "権限マスタを一括削除します。")
  @PreAuthorize("hasAuthority('permission:save')")
  @DeleteMapping("/permissions")
  public ApiResponse deleteAll(
      @Validated @RequestBody Requests<DeletePermissionRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val permissions = requests.stream().map(f -> modelMapper.map(f, Permission.class)).toList();

    // 一括削除する
    val deleted = permissionService.deleteAll(permissions);

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
  @Operation(summary = "権限マスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('permission:read')")
  @GetMapping("/permissions/export/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute SearchPermissionRequest request,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(request, PermissionCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      permissionService.writeToOutputStream(outputStream, criteria, PermissionCsv.class);
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
  @Operation(summary = "権限マスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('permission:read')")
  @PostMapping("/permissions/export/{filename:.+\\.csv}")
  public void searchByPost(
      @PathVariable String filename,
      @RequestBody SearchPermissionRequest request,
      HttpServletResponse response)
      throws IOException {
    downloadCsv(filename, request, response);
  }
}

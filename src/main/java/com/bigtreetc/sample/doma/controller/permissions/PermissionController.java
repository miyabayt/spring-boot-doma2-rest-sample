package com.bigtreetc.sample.doma.controller.permissions;

import static com.bigtreetc.sample.doma.base.util.TypeUtils.toListType;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.util.CsvUtils;
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
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "権限")
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
   * 権限を登録します。
   *
   * @param request
   * @param errors
   * @return
   */
  @Operation(summary = "権限登録", description = "権限を登録します。")
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
   * 権限を一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "権限一括登録", description = "権限を一括登録します。")
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
   * 権限を検索します。
   *
   * @param request
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "権限検索", description = "権限を検索します。")
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
   * 権限を検索します。（POST版）
   *
   * @param request
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "権限検索", description = "権限を検索します。")
  @PreAuthorize("hasAuthority('permission:read')")
  @PostMapping(value = "/permissions/search")
  public ApiResponse searchByPost(
      @RequestBody SearchPermissionRequest request, @Parameter(hidden = true) Pageable pageable) {
    return search(request, pageable);
  }

  /**
   * 権限を取得します。
   *
   * @param permissionId
   * @return
   */
  @Operation(summary = "権限取得", description = "権限を取得します。")
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
   * 権限を更新します。
   *
   * @param request
   * @param errors
   * @return
   */
  @Operation(summary = "権限更新", description = "権限を更新します。")
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
   * 権限を一括更新します。
   *
   * @param requests
   * @param errors
   * @return
   */
  @Operation(summary = "権限一括更新", description = "権限を一括更新します。")
  @PreAuthorize("hasAuthority('permission:save')")
  @PutMapping("/permission")
  public ApiResponse update(
      @Validated @RequestBody List<PermissionRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val permissions =
        requests.stream().map(f -> modelMapper.map(f, Permission.class)).collect(toList());

    // 更新する
    val updated = permissionService.updateAll(permissions);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * 権限を削除します。
   *
   * @param id
   * @return
   */
  @Operation(summary = "権限削除", description = "権限を削除します。")
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
   * 権限を一括削除します。
   *
   * @param requests
   * @param errors
   * @return
   */
  @Operation(summary = "権限一括削除", description = "権限を一括削除します。")
  @PreAuthorize("hasAuthority('permission:save')")
  @DeleteMapping("/permissions")
  public ApiResponse deleteAll(
      @Validated @RequestBody Requests<DeletePermissionRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val permissions =
        requests.stream().map(f -> modelMapper.map(f, Permission.class)).collect(toList());

    // 一括削除する
    val deleted = permissionService.deleteAll(permissions);

    val response = new CountApiResponseImpl();
    response.success(deleted);

    return response;
  }

  /**
   * TSVファイルをダウンロードします。
   *
   * @param filename
   * @return
   */
  @Operation(summary = "権限CSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('permission:read')")
  @GetMapping("/permissions/export/{filename:.+\\.csv}")
  public ResponseEntity<Resource> downloadTsv(@PathVariable String filename) throws Exception {
    // ファイルを読み込む
    val permissions = permissionService.findAll(new PermissionCriteria(), Pageable.unpaged());

    // 詰め替える
    List<PermissionCsv> csvList =
        modelMapper.map(permissions.getContent(), toListType(PermissionCsv.class));

    val outputStream = CsvUtils.writeCsv(PermissionCsv.class, csvList);
    val resource = new ByteArrayResource(outputStream.toByteArray());
    return toResponseEntity(resource, filename, true);
  }
}

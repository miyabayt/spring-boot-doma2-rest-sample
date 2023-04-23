package com.bigtreetc.sample.doma.controller.codecategories;

import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.doma.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.CountApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.PageableApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.controller.codes.SearchCodeRequest;
import com.bigtreetc.sample.doma.domain.model.CodeCategory;
import com.bigtreetc.sample.doma.domain.model.CodeCategoryCriteria;
import com.bigtreetc.sample.doma.domain.service.CodeCategoryService;
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

@Tag(name = "コード分類マスタ")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CodeCategoryController extends AbstractRestController {

  @NonNull final CodeCategoryRequestValidator codeCategoryRequestValidator;

  @NonNull final CodeCategoryService codeCategoryService;

  @InitBinder
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(codeCategoryRequestValidator);
  }

  /**
   * コード分類マスタを登録します。
   *
   * @param request
   * @param errors
   * @return
   */
  @Operation(summary = "コード分類マスタ登録", description = "コード分類マスタを登録します。")
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @PostMapping("/codeCategory")
  public ApiResponse create(@Validated @RequestBody CodeCategoryRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val codeCategory = modelMapper.map(request, CodeCategory.class);

    // 1件登録する
    val data = codeCategoryService.create(codeCategory);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コード分類マスタを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード分類マスタ一括登録", description = "コード分類マスタを一括登録します。")
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @PostMapping(value = "/codeCategories")
  public ApiResponse createAll(
      @Validated @RequestBody Requests<CodeCategoryRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val codeCategories =
        requests.stream().map(f -> modelMapper.map(f, CodeCategory.class)).toList();

    // 一括登録する
    val created = codeCategoryService.createAll(codeCategories);

    val response = new CountApiResponseImpl();
    response.success(created);

    return response;
  }

  /**
   * コード分類マスタを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "コード分類マスタ検索", description = "コード分類マスタを検索します。")
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @GetMapping("/codeCategories")
  public ApiResponse search(
      @ModelAttribute SearchCodeCategoryRequest request,
      @Parameter(hidden = true) Pageable pageable) {

    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, CodeCategoryCriteria.class);

    // 10件で区切って取得する
    val data = codeCategoryService.findAll(criteria, pageable);

    val response = new PageableApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コード分類マスタを検索します。（POST版）
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "コード分類マスタ検索", description = "コード分類マスタを検索します。")
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @PostMapping("/codeCategories/search")
  public ApiResponse searchByPost(
      @RequestBody SearchCodeCategoryRequest request, @Parameter(hidden = true) Pageable pageable) {
    return search(request, pageable);
  }

  /**
   * コード分類マスタを取得します。
   *
   * @param codeCategoryId
   * @return
   */
  @Operation(summary = "コード分類マスタ取得", description = "コード分類マスタを取得します。")
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @GetMapping("/codeCategory/{codeCategoryId}")
  public ApiResponse findOne(@PathVariable Long codeCategoryId) {
    // 1件取得する
    val data = codeCategoryService.findById(codeCategoryId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コード分類マスタを更新します。
   *
   * @param codeCategoryId
   * @param request
   * @return
   */
  @Operation(summary = "コード分類マスタ更新", description = "コード分類マスタを更新します。")
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @PutMapping("/codeCategory/{codeCategoryId}")
  public ApiResponse update(
      @PathVariable Long codeCategoryId,
      @Validated @RequestBody CodeCategoryRequest request,
      Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val codeCategory = modelMapper.map(request, CodeCategory.class);

    // 1件更新する
    codeCategory.setId(codeCategoryId);
    val data = codeCategoryService.update(codeCategory);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コード分類マスタを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード分類マスタ一括更新", description = "コード分類マスタを一括更新します。")
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @PutMapping(value = "/codeCategories")
  public ApiResponse updateAll(
      @Validated @RequestBody Requests<CodeCategoryRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val codeCategories =
        requests.stream().map(f -> modelMapper.map(f, CodeCategory.class)).collect(toList());

    // 一括更新する
    val updated = codeCategoryService.updateAll(codeCategories);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * コード分類マスタを削除します。
   *
   * @param codeCategoryId
   * @return
   */
  @Operation(summary = "コード分類マスタ削除", description = "コード分類マスタを削除します。")
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @DeleteMapping("/codeCategory/{codeCategoryId}")
  public ApiResponse delete(@PathVariable Long codeCategoryId) {
    // 1件取得する
    val data = codeCategoryService.delete(codeCategoryId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コード分類マスタを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード分類マスタ一括削除", description = "コード分類マスタを一括削除します。")
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @DeleteMapping(value = "/codeCategories")
  public ApiResponse deleteAll(
      @Validated @RequestBody Requests<DeleteCodeCategoryRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val codeCategories =
        requests.stream().map(f -> modelMapper.map(f, CodeCategory.class)).collect(toList());

    // 一括削除する
    val deleted = codeCategoryService.deleteAll(codeCategories);

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
  @Operation(summary = "コード分類マスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @GetMapping("/codeCategories/export/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute SearchCodeRequest request,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(request, CodeCategoryCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      codeCategoryService.writeToOutputStream(outputStream, criteria, CodeCategoryCsv.class);
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
  @Operation(summary = "コード分類マスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @PostMapping("/codeCategories/export/{filename:.+\\.csv}")
  public void searchByPost(
      @PathVariable String filename,
      @RequestBody SearchCodeRequest request,
      HttpServletResponse response)
      throws IOException {
    downloadCsv(filename, request, response);
  }
}

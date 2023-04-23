package com.bigtreetc.sample.doma.controller.codes;

import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.doma.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.CountApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.PageableApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.domain.model.Code;
import com.bigtreetc.sample.doma.domain.model.CodeCriteria;
import com.bigtreetc.sample.doma.domain.service.CodeService;
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

@Tag(name = "コードマスタ")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CodeController extends AbstractRestController {

  @NonNull final CodeRequestValidator codeRequestValidator;

  @NonNull final CodeService codeService;

  @InitBinder
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(codeRequestValidator);
  }

  /**
   * コードマスタを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "コードマスタ登録", description = "コードマスタを登録します。")
  @PreAuthorize("hasAuthority('code:save')")
  @PostMapping("/code")
  public ApiResponse create(@Validated @RequestBody CodeRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val code = modelMapper.map(request, Code.class);

    // 1件登録する
    val data = codeService.create(code);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コードマスタを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コードマスタ一括登録", description = "コードマスタを一括登録します。")
  @PreAuthorize("hasAuthority('code:save')")
  @PostMapping(value = "/codes")
  public ApiResponse createAll(
      @Validated @RequestBody Requests<CodeRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val codes = requests.stream().map(f -> modelMapper.map(f, Code.class)).toList();

    // 一括登録する
    val created = codeService.createAll(codes);

    val response = new CountApiResponseImpl();
    response.success(created);

    return response;
  }

  /**
   * コードマスタを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "コードマスタ検索", description = "コードマスタを検索します。")
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/codes")
  public ApiResponse search(
      @ModelAttribute SearchCodeRequest request, @Parameter(hidden = true) Pageable pageable) {
    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, CodeCriteria.class);

    // 10件で区切って取得する
    val data = codeService.findAll(criteria, pageable);

    val response = new PageableApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コードマスタを検索します。（POST版）
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "コードマスタ検索", description = "コードマスタを検索します。")
  @PreAuthorize("hasAuthority('code:read')")
  @PostMapping("/codes/search")
  public ApiResponse searchByPost(
      @RequestBody SearchCodeRequest request, @Parameter(hidden = true) Pageable pageable) {
    return search(request, pageable);
  }

  /**
   * コードマスタを取得します。
   *
   * @param codeId
   * @return
   */
  @Operation(summary = "コードマスタ取得", description = "コードマスタを取得します。")
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/code/{codeId}")
  public ApiResponse findOne(@PathVariable Long codeId) {
    // 1件取得する
    val data = codeService.findById(codeId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コードマスタを更新します。
   *
   * @param codeId
   * @param request
   * @return
   */
  @Operation(summary = "コードマスタ更新", description = "コードマスタを更新します。")
  @PreAuthorize("hasAuthority('code:save')")
  @PutMapping("/code/{codeId}")
  public ApiResponse update(
      @PathVariable Long codeId, @Validated @RequestBody CodeRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val code = modelMapper.map(request, Code.class);

    // 1件更新する
    code.setId(codeId);
    val data = codeService.update(code);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コードマスタを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コードマスタ一括更新", description = "コードマスタを一括更新します。")
  @PreAuthorize("hasAuthority('code:save')")
  @PutMapping(value = "/codes")
  public ApiResponse updateAll(
      @Validated @RequestBody Requests<CodeRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val codes = requests.stream().map(f -> modelMapper.map(f, Code.class)).collect(toList());

    // 一括更新する
    val updated = codeService.updateAll(codes);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * コードマスタを削除します。
   *
   * @param codeId
   * @return
   */
  @Operation(summary = "コードマスタ削除", description = "コードマスタを削除します。")
  @PreAuthorize("hasAuthority('code:save')")
  @DeleteMapping("/code/{codeId}")
  public ApiResponse delete(@PathVariable Long codeId) {
    // 1件取得する
    val data = codeService.delete(codeId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * コードマスタを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コードマスタ一括削除", description = "コードマスタを一括削除します。")
  @PreAuthorize("hasAuthority('code:save')")
  @DeleteMapping(value = "/codes")
  public ApiResponse deleteAll(
      @Validated @RequestBody Requests<DeleteCodeRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val codes = requests.stream().map(f -> modelMapper.map(f, Code.class)).collect(toList());

    // 一括削除する
    val deleted = codeService.deleteAll(codes);

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
  @Operation(summary = "コードマスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/codes/export/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute SearchCodeRequest request,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(request, CodeCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      codeService.writeToOutputStream(outputStream, criteria, CodeCsv.class);
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
  @Operation(summary = "コードマスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('code:read')")
  @PostMapping("/codes/export/{filename:.+\\.csv}")
  public void searchByPost(
      @PathVariable String filename,
      @RequestBody SearchCodeRequest request,
      HttpServletResponse response)
      throws IOException {
    downloadCsv(filename, request, response);
  }
}

package com.bigtreetc.sample.doma.controller.codes;

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
import com.bigtreetc.sample.doma.domain.model.Code;
import com.bigtreetc.sample.doma.domain.model.CodeCriteria;
import com.bigtreetc.sample.doma.domain.service.CodeService;
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

@Tag(name = "コード")
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
   * コードを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "コード登録", description = "コードを登録します。")
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
   * コードを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード一括登録", description = "コードを一括登録します。")
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
   * コードを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "コード検索", description = "コードを検索します。")
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/codes")
  public ApiResponse findAll(
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
   * コードを取得します。
   *
   * @param codeId
   * @return
   */
  @Operation(summary = "コード取得", description = "コードを取得します。")
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
   * コードを更新します。
   *
   * @param codeId
   * @param request
   * @return
   */
  @Operation(summary = "コード更新", description = "コードを更新します。")
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
   * コードを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード一括更新", description = "コードを一括更新します。")
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
   * コードを削除します。
   *
   * @param codeId
   * @return
   */
  @Operation(summary = "コード削除", description = "コードを削除します。")
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
   * コードを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード一括削除", description = "コードを一括削除します。")
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
   * @return
   */
  @Operation(summary = "コードCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/codes/export/{filename:.+\\.csv}")
  public ResponseEntity<Resource> downloadCsv(@PathVariable String filename) throws Exception {
    val codes = codeService.findAll(new CodeCriteria(), Pageable.unpaged());

    // 詰め替える
    List<CodeCsv> csvList = modelMapper.map(codes.getContent(), toListType(CodeCsv.class));

    val outputStream = CsvUtils.writeCsv(CodeCsv.class, csvList);
    val resource = new ByteArrayResource(outputStream.toByteArray());
    return toResponseEntity(resource, filename, true);
  }
}

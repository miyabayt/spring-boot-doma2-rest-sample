package com.bigtreetc.sample.doma.controller.mailtemplates;

import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.doma.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.CountApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.PageableApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.domain.model.MailTemplate;
import com.bigtreetc.sample.doma.domain.model.MailTemplateCriteria;
import com.bigtreetc.sample.doma.domain.service.MailTemplateService;
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

@Tag(name = "メールテンプレート")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MailTemplateController extends AbstractRestController {

  @NonNull final MailTemplateRequestValidator mailTemplatesRequestValidator;

  @NonNull final MailTemplateService mailTemplateService;

  @InitBinder
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(mailTemplatesRequestValidator);
  }

  /**
   * メールテンプレートを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "メールテンプレート登録", description = "メールテンプレートを登録します。")
  @PreAuthorize("hasAuthority('mailTemplates:save')")
  @PostMapping("/mailTemplate")
  public ApiResponse create(@Validated @RequestBody MailTemplateRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val mailTemplates = modelMapper.map(request, MailTemplate.class);

    // 1件登録する
    val data = mailTemplateService.create(mailTemplates);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * メールテンプレートを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "メールテンプレート一括登録", description = "メールテンプレートを一括登録します。")
  @PreAuthorize("hasAuthority('mailTemplates:save')")
  @PostMapping(value = "/mailTemplates")
  public ApiResponse createAll(
      @Validated @RequestBody Requests<MailTemplateRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val mailTemplates = requests.stream().map(f -> modelMapper.map(f, MailTemplate.class)).toList();

    // 一括登録する
    val created = mailTemplateService.createAll(mailTemplates);

    val response = new CountApiResponseImpl();
    response.success(created);

    return response;
  }

  /**
   * メールテンプレートを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "メールテンプレート検索", description = "メールテンプレートを検索します。")
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @GetMapping("/mailTemplates")
  public ApiResponse search(
      @ModelAttribute SearchMailTemplateRequest request,
      @Parameter(hidden = true) Pageable pageable) {
    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, MailTemplateCriteria.class);

    // 10件で区切って取得する
    val data = mailTemplateService.findAll(criteria, pageable);

    val response = new PageableApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * メールテンプレートを検索します。（POST版）
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "メールテンプレート検索", description = "メールテンプレートを検索します。")
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @PostMapping("/mailTemplates/search")
  public ApiResponse searchByPost(
      @RequestBody SearchMailTemplateRequest request, @Parameter(hidden = true) Pageable pageable) {
    return search(request, pageable);
  }

  /**
   * メールテンプレートを取得します。
   *
   * @param mailTemplateId
   * @return
   */
  @Operation(summary = "メールテンプレート取得", description = "メールテンプレートを取得します。")
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @GetMapping("/mailTemplate/{mailTemplateId}")
  public ApiResponse findOne(@PathVariable Long mailTemplateId) {
    // 1件取得する
    val data = mailTemplateService.findById(mailTemplateId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param mailTemplateId
   * @param request
   * @return
   */
  @Operation(summary = "メールテンプレート更新", description = "メールテンプレートを更新します。")
  @PreAuthorize("hasAuthority('mailTemplates:save')")
  @PutMapping("/mailTemplate/{mailTemplateId}")
  public ApiResponse update(
      @PathVariable Long mailTemplateId,
      @Validated @RequestBody MailTemplateRequest request,
      Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val mailTemplates = modelMapper.map(request, MailTemplate.class);

    // 1件更新する
    mailTemplates.setId(mailTemplateId);
    val data = mailTemplateService.update(mailTemplates);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * メールテンプレートを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "メールテンプレート一括更新", description = "メールテンプレートを一括更新します。")
  @PreAuthorize("hasAuthority('mailTemplates:save')")
  @PutMapping(value = "/mailTemplates")
  public ApiResponse updateAll(
      @Validated @RequestBody Requests<MailTemplateRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val mailTemplates = requests.stream().map(f -> modelMapper.map(f, MailTemplate.class)).toList();

    // 一括更新する
    val updated = mailTemplateService.updateAll(mailTemplates);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * メールテンプレートを削除します。
   *
   * @param mailTemplateId
   * @return
   */
  @Operation(summary = "メールテンプレート削除", description = "メールテンプレートを削除します。")
  @PreAuthorize("hasAuthority('mailTemplates:save')")
  @DeleteMapping("/mailTemplate/{mailTemplateId}")
  public ApiResponse delete(@PathVariable Long mailTemplateId) {
    // 1件取得する
    val data = mailTemplateService.delete(mailTemplateId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * メールテンプレートを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "メールテンプレート一括削除", description = "メールテンプレートを一括削除します。")
  @PreAuthorize("hasAuthority('mailTemplates:save')")
  @DeleteMapping(value = "/mailTemplates")
  public ApiResponse deleteAll(
      @Validated @RequestBody Requests<DeleteMailTemplateRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val mailTemplates = requests.stream().map(f -> modelMapper.map(f, MailTemplate.class)).toList();

    // 一括削除する
    val deleted = mailTemplateService.deleteAll(mailTemplates);

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
  @Operation(summary = "メールテンプレートCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @GetMapping("/mailTemplates/export/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute SearchMailTemplateRequest request,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(request, MailTemplateCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      mailTemplateService.writeToOutputStream(outputStream, criteria, MailTemplateCsv.class);
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
  @Operation(summary = "メールテンプレートCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @PostMapping("/mailTemplates/export/{filename:.+\\.csv}")
  public void searchByPost(
      @PathVariable String filename,
      @RequestBody SearchMailTemplateRequest request,
      HttpServletResponse response)
      throws IOException {
    downloadCsv(filename, request, response);
  }
}

package com.bigtreetc.sample.doma.controller.mailtemplates;

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
import com.bigtreetc.sample.doma.domain.model.MailTemplate;
import com.bigtreetc.sample.doma.domain.model.MailTemplateCriteria;
import com.bigtreetc.sample.doma.domain.service.MailTemplateService;
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

@Tag(name = "メールテンプレート")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MailTemplateController extends AbstractRestController {

  @NonNull final MailTemplateRequestValidator mailTemplatesRequestValidator;

  @NonNull final MailTemplateService mailTemplatesService;

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
    val data = mailTemplatesService.create(mailTemplates);

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
    val created = mailTemplatesService.createAll(mailTemplates);

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
  @PreAuthorize("hasAuthority('mailTemplates:read')")
  @GetMapping("/mailTemplates")
  public ApiResponse search(
      @ModelAttribute SearchMailTemplateRequest request,
      @Parameter(hidden = true) Pageable pageable) {
    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, MailTemplateCriteria.class);

    // 10件で区切って取得する
    val data = mailTemplatesService.findAll(criteria, pageable);

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
  @PreAuthorize("hasAuthority('mailTemplates:read')")
  @PostMapping("/mailTemplates/search")
  public ApiResponse searchByPost(
      @RequestBody SearchMailTemplateRequest request, @Parameter(hidden = true) Pageable pageable) {
    return search(request, pageable);
  }

  /**
   * メールテンプレートを取得します。
   *
   * @param mailTemplatesId
   * @return
   */
  @Operation(summary = "メールテンプレート取得", description = "メールテンプレートを取得します。")
  @PreAuthorize("hasAuthority('mailTemplates:read')")
  @GetMapping("/mailTemplate/{mailTemplatesId}")
  public ApiResponse findOne(@PathVariable Long mailTemplatesId) {
    // 1件取得する
    val data = mailTemplatesService.findById(mailTemplatesId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param mailTemplatesId
   * @param request
   * @return
   */
  @Operation(summary = "メールテンプレート更新", description = "メールテンプレートを更新します。")
  @PreAuthorize("hasAuthority('mailTemplates:save')")
  @PutMapping("/mailTemplate/{mailTemplatesId}")
  public ApiResponse update(
      @PathVariable Long mailTemplatesId,
      @Validated @RequestBody MailTemplateRequest request,
      Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val mailTemplates = modelMapper.map(request, MailTemplate.class);

    // 1件更新する
    mailTemplates.setId(mailTemplatesId);
    val data = mailTemplatesService.update(mailTemplates);

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
    val mailTemplates =
        requests.stream().map(f -> modelMapper.map(f, MailTemplate.class)).collect(toList());

    // 一括更新する
    val updated = mailTemplatesService.updateAll(mailTemplates);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * メールテンプレートを削除します。
   *
   * @param mailTemplatesId
   * @return
   */
  @Operation(summary = "メールテンプレート削除", description = "メールテンプレートを削除します。")
  @PreAuthorize("hasAuthority('mailTemplates:save')")
  @DeleteMapping("/mailTemplate/{mailTemplatesId}")
  public ApiResponse delete(@PathVariable Long mailTemplatesId) {
    // 1件取得する
    val data = mailTemplatesService.delete(mailTemplatesId);

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
    val mailTemplates =
        requests.stream().map(f -> modelMapper.map(f, MailTemplate.class)).collect(toList());

    // 一括削除する
    val deleted = mailTemplatesService.deleteAll(mailTemplates);

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
  @Operation(summary = "メールテンプレートCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('mailTemplates:read')")
  @GetMapping("/mailTemplates/export/{filename:.+\\.csv}")
  public ResponseEntity<Resource> downloadCsv(@PathVariable String filename) throws Exception {
    val mailTemplates =
        mailTemplatesService.findAll(new MailTemplateCriteria(), Pageable.unpaged());

    // 詰め替える
    List<MailTemplateCsv> csvList =
        modelMapper.map(mailTemplates.getContent(), toListType(MailTemplateCsv.class));

    val outputStream = CsvUtils.writeCsv(MailTemplateCsv.class, csvList);
    val resource = new ByteArrayResource(outputStream.toByteArray());
    return toResponseEntity(resource, filename, true);
  }
}

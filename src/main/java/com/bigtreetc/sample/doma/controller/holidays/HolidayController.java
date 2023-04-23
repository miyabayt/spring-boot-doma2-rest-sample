package com.bigtreetc.sample.doma.controller.holidays;

import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.doma.base.exception.ValidationErrorException;
import com.bigtreetc.sample.doma.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.doma.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.doma.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.doma.base.web.controller.api.response.CountApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.PageableApiResponseImpl;
import com.bigtreetc.sample.doma.base.web.controller.api.response.SimpleApiResponseImpl;
import com.bigtreetc.sample.doma.domain.model.Holiday;
import com.bigtreetc.sample.doma.domain.model.HolidayCriteria;
import com.bigtreetc.sample.doma.domain.service.HolidayService;
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

@Tag(name = "祝日マスタ")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class HolidayController extends AbstractRestController {

  @NonNull final HolidayRequestValidator holidayRequestValidator;

  @NonNull final HolidayService holidayService;

  @InitBinder
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(holidayRequestValidator);
  }

  /**
   * 祝日マスタを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "祝日マスタ登録", description = "祝日マスタを登録します。")
  @PreAuthorize("hasAuthority('holiday:save')")
  @PostMapping("/holiday")
  public ApiResponse create(@Validated @RequestBody HolidayRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val holiday = modelMapper.map(request, Holiday.class);

    // 1件登録する
    val data = holidayService.create(holiday);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 祝日マスタを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "祝日マスタ一括登録", description = "祝日マスタを一括登録します。")
  @PreAuthorize("hasAuthority('holiday:save')")
  @PostMapping(value = "/holidays")
  public ApiResponse createAll(
      @Validated @RequestBody Requests<HolidayRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からモデルを作成する
    val holidays = requests.stream().map(f -> modelMapper.map(f, Holiday.class)).toList();

    // 一括登録する
    val created = holidayService.createAll(holidays);

    val response = new CountApiResponseImpl();
    response.success(created);

    return response;
  }

  /**
   * 祝日マスタを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "祝日マスタ検索", description = "祝日マスタを検索します。")
  @PreAuthorize("hasAuthority('holiday:read')")
  @GetMapping("/holidays")
  public ApiResponse search(
      @ModelAttribute SearchHolidayRequest request, @Parameter(hidden = true) Pageable pageable) {
    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, HolidayCriteria.class);

    // 10件で区切って取得する
    val data = holidayService.findAll(criteria, pageable);

    val response = new PageableApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 祝日マスタを検索します。（POST版）
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "祝日マスタ検索", description = "祝日マスタを検索します。")
  @PreAuthorize("hasAuthority('holiday:read')")
  @PostMapping("/holidays/search")
  public ApiResponse searchByPost(
      @RequestBody SearchHolidayRequest request, @Parameter(hidden = true) Pageable pageable) {
    return search(request, pageable);
  }

  /**
   * 祝日マスタを取得します。
   *
   * @param holidayId
   * @return
   */
  @Operation(summary = "祝日マスタ取得", description = "祝日マスタを取得します。")
  @PreAuthorize("hasAuthority('holiday:read')")
  @GetMapping("/holiday/{holidayId}")
  public ApiResponse findOne(@PathVariable Long holidayId) {
    // 1件取得する
    val data = holidayService.findById(holidayId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 祝日マスタを更新します。
   *
   * @param holidayId
   * @param request
   * @return
   */
  @Operation(summary = "祝日マスタ更新", description = "祝日マスタを更新します。")
  @PreAuthorize("hasAuthority('holiday:save')")
  @PutMapping("/holiday/{holidayId}")
  public ApiResponse update(
      @PathVariable Long holidayId, @Validated @RequestBody HolidayRequest request, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val holiday = modelMapper.map(request, Holiday.class);

    // 1件更新する
    holiday.setId(holidayId);
    val data = holidayService.update(holiday);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 祝日マスタを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "祝日マスタ一括更新", description = "祝日マスタを一括更新します。")
  @PreAuthorize("hasAuthority('holiday:save')")
  @PutMapping(value = "/holidays")
  public ApiResponse updateAll(
      @Validated @RequestBody Requests<HolidayRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val holidays = requests.stream().map(f -> modelMapper.map(f, Holiday.class)).collect(toList());

    // 一括更新する
    val updated = holidayService.updateAll(holidays);

    val response = new CountApiResponseImpl();
    response.success(updated);

    return response;
  }

  /**
   * 祝日マスタを削除します。
   *
   * @param holidayId
   * @return
   */
  @Operation(summary = "祝日マスタ削除", description = "祝日マスタを削除します。")
  @PreAuthorize("hasAuthority('holiday:save')")
  @DeleteMapping("/holiday/{holidayId}")
  public ApiResponse delete(@PathVariable Long holidayId) {
    // 1件取得する
    val data = holidayService.delete(holidayId);

    val response = new SimpleApiResponseImpl();
    response.success(data);

    return response;
  }

  /**
   * 祝日マスタを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "祝日マスタ一括削除", description = "祝日マスタを一括削除します。")
  @PreAuthorize("hasAuthority('holiday:save')")
  @DeleteMapping(value = "/holidays")
  public ApiResponse deleteAll(
      @Validated @RequestBody Requests<DeleteHolidayRequest> requests, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    // 入力値からDTOを作成する
    val holidays = requests.stream().map(f -> modelMapper.map(f, Holiday.class)).collect(toList());

    // 一括削除する
    val deleted = holidayService.deleteAll(holidays);

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
  @Operation(summary = "祝日マスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('holiday:read')")
  @GetMapping("/holidays/export/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute SearchHolidayRequest request,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(request, HolidayCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      holidayService.writeToOutputStream(outputStream, criteria, HolidayCsv.class);
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
  @Operation(summary = "祝日マスタCSV出力", description = "CSVファイルを出力します。")
  @PreAuthorize("hasAuthority('holiday:read')")
  @PostMapping("/holidays/export/{filename:.+\\.csv}")
  public void searchByPost(
      @PathVariable String filename,
      @RequestBody SearchHolidayRequest request,
      HttpServletResponse response)
      throws IOException {
    downloadCsv(filename, request, response);
  }
}

package com.bigtreetc.sample.doma.domain.service;

import com.bigtreetc.sample.doma.base.domain.service.BaseTransactionalService;
import com.bigtreetc.sample.doma.base.util.CsvUtils;
import com.bigtreetc.sample.doma.domain.model.Holiday;
import com.bigtreetc.sample.doma.domain.model.HolidayCriteria;
import com.bigtreetc.sample.doma.domain.repository.HolidayRepository;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/** 祝日サービス */
@RequiredArgsConstructor
@Service
public class HolidayService extends BaseTransactionalService {

  @NonNull final HolidayRepository holidayRepository;

  /**
   * 祝日マスタを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<Holiday> findAll(HolidayCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return holidayRepository.findAll(criteria, pageable);
  }

  /**
   * 祝日マスタを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Holiday> findOne(HolidayCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return holidayRepository.findOne(criteria);
  }

  /**
   * 祝日マスタを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Holiday findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return holidayRepository.findById(id);
  }

  /**
   * 祝日マスタを登録します。
   *
   * @param inputHoliday
   * @return
   */
  public Holiday create(final Holiday inputHoliday) {
    Assert.notNull(inputHoliday, "inputHoliday must not be null");
    return holidayRepository.create(inputHoliday);
  }

  /**
   * コードマスタを一括登録します。
   *
   * @param holidays
   * @return
   */
  public int createAll(final List<Holiday> holidays) {
    Assert.notNull(holidays, "holidays must not be null");
    return holidayRepository.createAll(holidays);
  }

  /**
   * 祝日マスタを更新します。
   *
   * @param inputHoliday
   * @return
   */
  public Holiday update(final Holiday inputHoliday) {
    Assert.notNull(inputHoliday, "inputHoliday must not be null");
    return holidayRepository.update(inputHoliday);
  }

  /**
   * コードマスタを一括更新します。
   *
   * @param holidays
   * @return
   */
  public int updateAll(final List<Holiday> holidays) {
    Assert.notNull(holidays, "holiday must not be null");
    return holidayRepository.updateAll(holidays);
  }

  /**
   * 祝日マスタを削除します。
   *
   * @return
   */
  public Holiday delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return holidayRepository.delete(id);
  }

  /**
   * 祝日マスタを一括削除します。
   *
   * @param holidays
   * @return
   */
  public int deleteAll(final List<Holiday> holidays) {
    Assert.notNull(holidays, "holiday must not be null");
    return holidayRepository.deleteAll(holidays);
  }

  /**
   * 祝日マスタを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(
      OutputStream outputStream, HolidayCriteria criteria, Class<?> clazz) throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = holidayRepository.findAll(criteria)) {
      CsvUtils.writeCsv(outputStream, clazz, data, holiday -> modelMapper.map(holiday, clazz));
    }
  }
}

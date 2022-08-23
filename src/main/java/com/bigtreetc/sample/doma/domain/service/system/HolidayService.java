package com.bigtreetc.sample.doma.domain.service.system;

import com.bigtreetc.sample.doma.domain.model.system.Holiday;
import com.bigtreetc.sample.doma.domain.model.system.HolidayCriteria;
import com.bigtreetc.sample.doma.domain.repository.system.HolidayRepository;
import com.bigtreetc.sample.doma.domain.service.BaseTransactionalService;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
   * 祝日を複数取得します。
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
   * 祝日を取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Holiday> findOne(HolidayCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return holidayRepository.findOne(criteria);
  }

  /**
   * 祝日を取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Holiday findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return holidayRepository.findById(id);
  }

  /**
   * 祝日を登録します。
   *
   * @param inputHoliday
   * @return
   */
  public Holiday create(final Holiday inputHoliday) {
    Assert.notNull(inputHoliday, "inputHoliday must not be null");
    return holidayRepository.create(inputHoliday);
  }

  /**
   * コードを一括登録します。
   *
   * @param holidays
   * @return
   */
  public int createAll(final List<Holiday> holidays) {
    Assert.notNull(holidays, "holidays must not be null");
    return holidayRepository.createAll(holidays);
  }

  /**
   * 祝日を更新します。
   *
   * @param inputHoliday
   * @return
   */
  public Holiday update(final Holiday inputHoliday) {
    Assert.notNull(inputHoliday, "inputHoliday must not be null");
    return holidayRepository.update(inputHoliday);
  }

  /**
   * コードを一括更新します。
   *
   * @param holidays
   * @return
   */
  public int updateAll(final List<Holiday> holidays) {
    Assert.notNull(holidays, "holiday must not be null");
    return holidayRepository.updateAll(holidays);
  }

  /**
   * 祝日を削除します。
   *
   * @return
   */
  public Holiday delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return holidayRepository.delete(id);
  }

  /**
   * コードを一括削除します。
   *
   * @param holidays
   * @return
   */
  public int deleteAll(final List<Holiday> holidays) {
    Assert.notNull(holidays, "holiday must not be null");
    return holidayRepository.deleteAll(holidays);
  }
}

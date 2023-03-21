package com.bigtreetc.sample.doma.domain.repository;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.boot.Pageables.toSelectOptions;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.dao.HolidayDao;
import com.bigtreetc.sample.doma.domain.model.Holiday;
import com.bigtreetc.sample.doma.domain.model.HolidayCriteria;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** 祝日リポジトリ */
@RequiredArgsConstructor
@Repository
public class HolidayRepository {

  @NonNull final HolidayDao holidayDao;

  /**
   * 祝日を複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Holiday> findAll(HolidayCriteria criteria, Pageable pageable) {
    val options = toSelectOptions(pageable).count();
    val data = holidayDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * 祝日を取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Holiday> findOne(HolidayCriteria criteria) {
    return holidayDao.select(criteria);
  }

  /**
   * 祝日を取得します。
   *
   * @return
   */
  public Holiday findById(final Long id) {
    return holidayDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("holiday_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * 祝日を登録します。
   *
   * @param holiday
   * @return
   */
  public Holiday create(final Holiday holiday) {
    holidayDao.insert(holiday);
    return holiday;
  }

  /**
   * 祝日を一括登録します。
   *
   * @param holidays
   * @return
   */
  public int createAll(final List<Holiday> holidays) {
    val inserted = holidayDao.insert(holidays);
    return Arrays.stream(inserted).sum();
  }

  /**
   * 祝日を更新します。
   *
   * @param holiday
   * @return
   */
  public Holiday update(final Holiday holiday) {
    int updated = holidayDao.update(holiday);

    if (updated < 1) {
      throw new NoDataFoundException("holiday_id=" + holiday.getId() + " のデータが見つかりません。");
    }

    return holiday;
  }

  /**
   * 祝日を一括更新します。
   *
   * @param holidays
   * @return
   */
  public int updateAll(final List<Holiday> holidays) {
    val updated = holidayDao.update(holidays);
    return Arrays.stream(updated).sum();
  }

  /**
   * 祝日を削除します。
   *
   * @return
   */
  public Holiday delete(final Long id) {
    val holiday =
        holidayDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("holiday_id=" + id + " のデータが見つかりません。"));

    int updated = holidayDao.delete(holiday);
    if (updated < 1) {
      throw new NoDataFoundException("holiday_id=" + id + " は更新できませんでした。");
    }

    return holiday;
  }

  /**
   * 祝日を一括削除します。
   *
   * @return
   */
  public int deleteAll(final List<Holiday> holidays) {
    val deleted = holidayDao.delete(holidays);
    return Arrays.stream(deleted).sum();
  }
}

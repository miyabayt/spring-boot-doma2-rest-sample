package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.Holiday;
import com.bigtreetc.sample.doma.domain.model.HolidayCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.message.Message;

@ConfigAutowireable
@Dao
public interface HolidayDao {

  /**
   * 祝日マスタを検索します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final HolidayCriteria criteria,
      final SelectOptions options,
      final Collector<Holiday, ?, R> collector);

  /**
   * 祝日マスタを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Holiday> selectAll(final HolidayCriteria criteria);

  /**
   * 祝日マスタを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Holiday> selectById(Long id);

  /**
   * 祝日マスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Holiday> select(HolidayCriteria criteria);

  /**
   * 祝日マスタを登録します。
   *
   * @param holiday
   * @return
   */
  @Insert
  int insert(Holiday holiday);

  /**
   * 祝日マスタを更新します。
   *
   * @param holiday
   * @return
   */
  @Update
  int update(Holiday holiday);

  /**
   * 祝日マスタを削除します。
   *
   * @param holiday
   * @return
   */
  @Delete
  int delete(Holiday holiday);

  /**
   * 祝日マスタを一括登録します。
   *
   * @param holidays
   * @return
   */
  @BatchInsert
  int[] insert(List<Holiday> holidays);

  /**
   * 祝日マスタを一括更新します。
   *
   * @param holidays
   * @return
   */
  @BatchUpdate
  int[] update(List<Holiday> holidays);

  /**
   * 祝日マスタを一括削除します。
   *
   * @param holidays
   * @return
   */
  @BatchDelete
  int[] delete(List<Holiday> holidays);
}

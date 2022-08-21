package com.bigtreetc.sample.doma.domain.dao.system;

import com.bigtreetc.sample.doma.domain.model.system.Holiday;
import com.bigtreetc.sample.doma.domain.model.system.HolidayCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface HolidayDao {

  /**
   * 祝日を取得します。
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
   * 祝日を1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Holiday> selectById(Long id);

  /**
   * 祝日を1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Holiday> select(HolidayCriteria criteria);

  /**
   * 祝日を登録します。
   *
   * @param holiday
   * @return
   */
  @Insert
  int insert(Holiday holiday);

  /**
   * 祝日を更新します。
   *
   * @param holiday
   * @return
   */
  @Update
  int update(Holiday holiday);

  /**
   * 祝日を削除します。
   *
   * @param holiday
   * @return
   */
  @Delete
  int delete(Holiday holiday);

  /**
   * 祝日を一括登録します。
   *
   * @param holidays
   * @return
   */
  @BatchInsert
  int[] insert(List<Holiday> holidays);

  /**
   * 祝日を一括更新します。
   *
   * @param holidays
   * @return
   */
  @BatchUpdate
  int[] update(List<Holiday> holidays);

  /**
   * 祝日を一括削除します。
   *
   * @param holidays
   * @return
   */
  @BatchDelete
  int[] delete(List<Holiday> holidays);
}

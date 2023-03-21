package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.Staff;
import com.bigtreetc.sample.doma.domain.model.StaffCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface StaffDao {

  /**
   * 担当者を取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final StaffCriteria criteria,
      final SelectOptions options,
      final Collector<Staff, ?, R> collector);

  /**
   * 担当者を1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Staff> selectById(Long id);

  /**
   * 担当者を1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Staff> select(StaffCriteria criteria);

  /**
   * 担当者を登録します。
   *
   * @param Staff
   * @return
   */
  @Insert
  int insert(Staff Staff);

  /**
   * 担当者を更新します。
   *
   * @param staff
   * @return
   */
  @Update
  int update(Staff staff);

  /**
   * 担当者を削除します。
   *
   * @param staff
   * @return
   */
  @Delete
  int delete(Staff staff);

  /**
   * 担当者を一括登録します。
   *
   * @param staffs
   * @return
   */
  @BatchInsert
  int[] insert(List<Staff> staffs);

  /**
   * 担当者を一括更新します。
   *
   * @param staffs
   * @return
   */
  @BatchUpdate
  int[] update(List<Staff> staffs);

  /**
   * 担当者を一括削除します。
   *
   * @param staffs
   * @return
   */
  @BatchDelete
  int[] delete(List<Staff> staffs);
}

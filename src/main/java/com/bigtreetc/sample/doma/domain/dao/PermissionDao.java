package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.Permission;
import com.bigtreetc.sample.doma.domain.model.PermissionCriteria;
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
public interface PermissionDao {

  /**
   * 権限マスタを検索します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final PermissionCriteria criteria,
      final SelectOptions options,
      final Collector<Permission, ?, R> collector);

  /**
   * 権限マスタを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Permission> selectAll(final PermissionCriteria criteria);

  /**
   * 権限マスタを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Permission> selectById(Long id);

  /**
   * 権限マスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Permission> select(PermissionCriteria criteria);

  /**
   * 権限マスタを登録します。
   *
   * @param permission
   * @return
   */
  @Insert
  int insert(Permission permission);

  /**
   * 権限マスタを更新します。
   *
   * @param permission
   * @return
   */
  @Update
  int update(Permission permission);

  /**
   * 権限マスタを削除します。
   *
   * @param permission
   * @return
   */
  @Delete
  int delete(Permission permission);

  /**
   * 権限マスタを一括登録します。
   *
   * @param permissions
   * @return
   */
  @BatchInsert
  int[] insert(List<Permission> permissions);

  /**
   * 権限マスタを一括更新します。
   *
   * @param permissions
   * @return
   */
  @BatchUpdate
  int[] update(List<Permission> permissions);

  /**
   * 権限マスタを一括削除します。
   *
   * @param permissions
   * @return
   */
  @BatchDelete
  int[] delete(List<Permission> permissions);
}

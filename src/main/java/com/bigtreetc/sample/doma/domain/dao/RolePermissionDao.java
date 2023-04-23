package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.RolePermission;
import com.bigtreetc.sample.doma.domain.model.RolePermissionCriteria;
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
public interface RolePermissionDao {

  /**
   * ロール権限紐付けを検索します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final RolePermissionCriteria criteria,
      final SelectOptions options,
      final Collector<RolePermission, ?, R> collector);

  /**
   * ロール権限紐付けを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<RolePermission> selectAll(final RolePermissionCriteria criteria);

  /**
   * ロール権限紐付けを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<RolePermission> selectById(Long id);

  /**
   * ロール権限紐付けを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<RolePermission> select(RolePermissionCriteria criteria);

  /**
   * ロール権限紐付けを登録します。
   *
   * @param rolePermission
   * @return
   */
  @Insert
  int insert(RolePermission rolePermission);

  /**
   * ロール権限紐付けを更新します。
   *
   * @param rolePermission
   * @return
   */
  @Update
  int update(RolePermission rolePermission);

  /**
   * ロール権限紐付けを削除します。
   *
   * @param rolePermission
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(RolePermission rolePermission);

  /**
   * ロール権限紐付けを一括削除します。
   *
   * @param rolePermissions
   * @return
   */
  @BatchUpdate
  int[] delete(List<RolePermission> rolePermissions);

  /**
   * ロール権限紐付けを一括登録します。
   *
   * @param rolePermissions
   * @return
   */
  @BatchInsert
  int[] insert(List<RolePermission> rolePermissions);
}

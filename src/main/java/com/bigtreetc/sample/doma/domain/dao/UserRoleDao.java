package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface UserRoleDao {

  /**
   * ユーザロールを検索します。
   *
   * @param userCriteria
   * @param permissionCriteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final UserCriteria userCriteria,
      final PermissionCriteria permissionCriteria,
      final SelectOptions options,
      final Collector<UserRole, ?, R> collector);

  /**
   * ユーザロールを検索します。
   *
   * @param id
   * @param collector
   * @param <R>
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectByUserId(Long id, final Collector<UserRole, ?, R> collector);

  /**
   * ユーザロールを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Permission> selectById(Long id);

  /**
   * ユーザロールを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Permission> select(PermissionCriteria criteria);

  /**
   * ユーザロールを登録します。
   *
   * @param userRole
   * @return
   */
  @Insert(exclude = {"roleName", "permissionCode", "permissionName"})
  int insert(UserRole userRole);

  /**
   * ユーザロールを更新します。
   *
   * @param userRole
   * @return
   */
  @Update(exclude = {"roleName", "permissionCode", "permissionName"})
  int update(UserRole userRole);

  /**
   * ユーザロールを削除します。
   *
   * @param userRole
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(UserRole userRole);

  /**
   * ユーザロールを一括登録します。
   *
   * @param userRoles
   * @return
   */
  @BatchInsert(exclude = {"roleName", "permissionCode", "permissionName"})
  int[] insert(List<UserRole> userRoles);
}

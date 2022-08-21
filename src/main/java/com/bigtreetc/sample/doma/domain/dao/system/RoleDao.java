package com.bigtreetc.sample.doma.domain.dao.system;

import com.bigtreetc.sample.doma.domain.model.system.Role;
import com.bigtreetc.sample.doma.domain.model.system.RoleCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface RoleDao {

  /**
   * ロールを取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final RoleCriteria criteria,
      final SelectOptions options,
      final Collector<Role, ?, R> collector);

  /**
   * ロールを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Role> selectById(Long id);

  /**
   * ロールを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Role> select(RoleCriteria criteria);

  /**
   * ロールを登録します。
   *
   * @param role
   * @return
   */
  @Insert
  int insert(Role role);

  /**
   * ロールを更新します。
   *
   * @param role
   * @return
   */
  @Update
  int update(Role role);

  /**
   * コードを削除します。
   *
   * @param role
   * @return
   */
  @Delete
  int delete(Role role);

  /**
   * コードを一括登録します。
   *
   * @param roles
   * @return
   */
  @BatchInsert
  int[] insert(List<Role> roles);

  /**
   * コードを一括更新します。
   *
   * @param roles
   * @return
   */
  @BatchUpdate
  int[] update(List<Role> roles);

  /**
   * コードを一括削除します。
   *
   * @param roles
   * @return
   */
  @BatchDelete
  int[] delete(List<Role> roles);
}

package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.Role;
import com.bigtreetc.sample.doma.domain.model.RoleCriteria;
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
public interface RoleDao {

  /**
   * ロールマスタを検索します。
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
   * ロールマスタを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Role> selectAll(final RoleCriteria criteria);

  /**
   * ロールマスタを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Role> selectById(Long id);

  /**
   * ロールマスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Role> select(RoleCriteria criteria);

  /**
   * ロールマスタを登録します。
   *
   * @param role
   * @return
   */
  @Insert
  int insert(Role role);

  /**
   * ロールマスタを更新します。
   *
   * @param role
   * @return
   */
  @Update
  int update(Role role);

  /**
   * ロールマスタを削除します。
   *
   * @param role
   * @return
   */
  @Delete
  int delete(Role role);

  /**
   * ロールマスタを一括登録します。
   *
   * @param roles
   * @return
   */
  @BatchInsert
  int[] insert(List<Role> roles);

  /**
   * ロールマスタを一括更新します。
   *
   * @param roles
   * @return
   */
  @BatchUpdate
  int[] update(List<Role> roles);

  /**
   * ロールマスタを一括削除します。
   *
   * @param roles
   * @return
   */
  @BatchDelete
  int[] delete(List<Role> roles);
}

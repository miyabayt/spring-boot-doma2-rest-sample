package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.User;
import com.bigtreetc.sample.doma.domain.model.UserCriteria;
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
public interface UserDao {

  /**
   * ユーザを検索します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final UserCriteria criteria,
      final SelectOptions options,
      final Collector<User, ?, R> collector);

  /**
   * ユーザを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<User> selectAll(final UserCriteria criteria);

  /**
   * ユーザを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<User> selectById(Long id);

  /**
   * ユーザを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<User> select(UserCriteria criteria);

  /**
   * ユーザを登録します。
   *
   * @param user
   * @return
   */
  @Insert
  int insert(User user);

  /**
   * ユーザを更新します。
   *
   * @param user
   * @return
   */
  @Update
  int update(User user);

  /**
   * ユーザを削除します。
   *
   * @param user
   * @return
   */
  @Delete
  int delete(User user);

  /**
   * ユーザを一括登録します。
   *
   * @param users
   * @return
   */
  @BatchInsert
  int[] insert(List<User> users);

  /**
   * ユーザを一括更新します。
   *
   * @param users
   * @return
   */
  @BatchUpdate
  int[] update(List<User> users);

  /**
   * ユーザを一括削除します。
   *
   * @param users
   * @return
   */
  @BatchDelete
  int[] delete(List<User> users);
}

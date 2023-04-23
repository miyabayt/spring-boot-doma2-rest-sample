package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.Code;
import com.bigtreetc.sample.doma.domain.model.CodeCriteria;
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
public interface CodeDao {

  /**
   * コードマスタを検索します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final CodeCriteria criteria,
      final SelectOptions options,
      final Collector<Code, ?, R> collector);

  /**
   * コードマスタを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Code> selectAll(final CodeCriteria criteria);

  /**
   * コードマスタを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Code> selectById(Long id);

  /**
   * コードマスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Code> select(CodeCriteria criteria);

  /**
   * コードマスタを登録します。
   *
   * @param Code
   * @return
   */
  @Insert
  int insert(Code Code);

  /**
   * コードマスタを更新します。
   *
   * @param code
   * @return
   */
  @Update
  int update(Code code);

  /**
   * コードマスタを削除します。
   *
   * @param code
   * @return
   */
  @Delete
  int delete(Code code);

  /**
   * コードマスタを一括登録します。
   *
   * @param codes
   * @return
   */
  @BatchInsert
  int[] insert(List<Code> codes);

  /**
   * コードマスタを一括更新します。
   *
   * @param codes
   * @return
   */
  @BatchUpdate
  int[] update(List<Code> codes);

  /**
   * コードマスタを一括削除します。
   *
   * @param codes
   * @return
   */
  @BatchDelete
  int[] delete(List<Code> codes);
}

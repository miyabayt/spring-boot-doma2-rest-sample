package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.CodeCategory;
import com.bigtreetc.sample.doma.domain.model.CodeCategoryCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface CodeCategoryDao {

  /**
   * コード分類マスタを取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final CodeCategoryCriteria criteria,
      final SelectOptions options,
      final Collector<CodeCategory, ?, R> collector);

  /**
   * コード分類マスタを全件取得します。
   *
   * @return
   */
  @Select
  List<CodeCategory> fetchAll();

  /**
   * コード分類マスタを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<CodeCategory> selectById(Long id);

  /**
   * コード分類マスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<CodeCategory> select(CodeCategoryCriteria criteria);

  /**
   * コード分類マスタを登録します。
   *
   * @param CodeCategory
   * @return
   */
  @Insert
  int insert(CodeCategory CodeCategory);

  /**
   * コード分類マスタを更新します。
   *
   * @param codeCategory
   * @return
   */
  @Update
  int update(CodeCategory codeCategory);

  /**
   * コード分類マスタを削除します。
   *
   * @param codeCategory
   * @return
   */
  @Delete
  int delete(CodeCategory codeCategory);

  /**
   * コード分類マスタを一括登録します。
   *
   * @param codes
   * @return
   */
  @BatchInsert
  int[] insert(List<CodeCategory> codes);

  /**
   * コード分類マスタを一括更新します。
   *
   * @param codes
   * @return
   */
  @BatchUpdate
  int[] update(List<CodeCategory> codes);

  /**
   * コード分類マスタを一括削除します。
   *
   * @param codes
   * @return
   */
  @BatchDelete
  int[] delete(List<CodeCategory> codes);
}

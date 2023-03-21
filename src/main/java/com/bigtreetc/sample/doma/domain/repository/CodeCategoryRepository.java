package com.bigtreetc.sample.doma.domain.repository;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.boot.Pageables.toSelectOptions;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.dao.CodeCategoryDao;
import com.bigtreetc.sample.doma.domain.model.CodeCategory;
import com.bigtreetc.sample.doma.domain.model.CodeCategoryCriteria;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** コード分類マスタリポジトリ */
@RequiredArgsConstructor
@Repository
public class CodeCategoryRepository {

  @NonNull final CodeCategoryDao codeCategoryDao;

  /**
   * コード分類マスタを全件取得します。
   *
   * @return
   */
  public List<CodeCategory> fetchAll() {
    val pageable = Pageable.unpaged();
    val options = toSelectOptions(pageable).count();
    return codeCategoryDao.selectAll(new CodeCategoryCriteria(), options, toList());
  }

  /**
   * コード分類マスタを複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<CodeCategory> findAll(CodeCategoryCriteria criteria, Pageable pageable) {
    val options = toSelectOptions(pageable).count();
    val data = codeCategoryDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * コード分類マスタを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<CodeCategory> findOne(CodeCategoryCriteria criteria) {
    return codeCategoryDao.select(criteria);
  }

  /**
   * コード分類マスタを取得します。
   *
   * @return
   */
  public CodeCategory findById(final Long id) {
    return codeCategoryDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("codeCategory_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * コード分類マスタを登録します。
   *
   * @param codeCategory
   * @return
   */
  public CodeCategory create(final CodeCategory codeCategory) {
    codeCategoryDao.insert(codeCategory);
    return codeCategory;
  }

  /**
   * コード分類マスタを一括登録します。
   *
   * @param codeCategories
   * @return
   */
  public int createAll(final List<CodeCategory> codeCategories) {
    val inserted = codeCategoryDao.insert(codeCategories);
    return Arrays.stream(inserted).sum();
  }

  /**
   * コード分類マスタを更新します。
   *
   * @param codeCategory
   * @return
   */
  public CodeCategory update(final CodeCategory codeCategory) {
    int updated = codeCategoryDao.update(codeCategory);

    if (updated < 1) {
      throw new NoDataFoundException("code_category_id=" + codeCategory.getId() + " のデータが見つかりません。");
    }

    return codeCategory;
  }

  /**
   * コード分類マスタを一括更新します。
   *
   * @param codeCategories
   * @return
   */
  public int updateAll(final List<CodeCategory> codeCategories) {
    val updated = codeCategoryDao.update(codeCategories);
    return Arrays.stream(updated).sum();
  }

  /**
   * コード分類マスタを削除します。
   *
   * @return
   */
  public CodeCategory delete(final Long id) {
    val codeCategory =
        codeCategoryDao
            .selectById(id)
            .orElseThrow(
                () -> new NoDataFoundException("code_category_id=" + id + " のデータが見つかりません。"));

    int updated = codeCategoryDao.delete(codeCategory);
    if (updated < 1) {
      throw new NoDataFoundException("code_category_id=" + id + " は更新できませんでした。");
    }

    return codeCategory;
  }

  /**
   * コード分類マスタを一括削除します。
   *
   * @return
   */
  public int deleteAll(final List<CodeCategory> codeCategories) {
    val deleted = codeCategoryDao.delete(codeCategories);
    return Arrays.stream(deleted).sum();
  }
}

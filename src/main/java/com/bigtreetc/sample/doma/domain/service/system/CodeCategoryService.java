package com.bigtreetc.sample.doma.domain.service.system;

import com.bigtreetc.sample.doma.domain.model.system.CodeCategory;
import com.bigtreetc.sample.doma.domain.model.system.CodeCategoryCriteria;
import com.bigtreetc.sample.doma.domain.repository.system.CodeCategoryRepository;
import com.bigtreetc.sample.doma.domain.service.BaseTransactionalService;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/** コード分類サービス */
@RequiredArgsConstructor
@Service
public class CodeCategoryService extends BaseTransactionalService {

  @NonNull final CodeCategoryRepository codeCategoryRepository;

  /**
   * コード分類を全件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public List<CodeCategory> fetchAll() {
    return codeCategoryRepository.fetchAll();
  }

  /**
   * コード分類を複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<CodeCategory> findAll(CodeCategoryCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeCategoryRepository.findAll(criteria, pageable);
  }

  /**
   * コード分類を取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<CodeCategory> findOne(CodeCategoryCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeCategoryRepository.findOne(criteria);
  }

  /**
   * コード分類を取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public CodeCategory findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeCategoryRepository.findById(id);
  }

  /**
   * コード分類を登録します。
   *
   * @param inputCodeCategory
   * @return
   */
  public CodeCategory create(final CodeCategory inputCodeCategory) {
    Assert.notNull(inputCodeCategory, "inputCodeCategory must not be null");
    return codeCategoryRepository.create(inputCodeCategory);
  }

  /**
   * コード分類を一括登録します。
   *
   * @param codeCategories
   * @return
   */
  public int createAll(final List<CodeCategory> codeCategories) {
    Assert.notNull(codeCategories, "codeCategories must not be null");
    return codeCategoryRepository.createAll(codeCategories);
  }

  /**
   * コード分類を更新します。
   *
   * @param inputCodeCategory
   * @return
   */
  public CodeCategory update(final CodeCategory inputCodeCategory) {
    Assert.notNull(inputCodeCategory, "inputCodeCategory must not be null");
    return codeCategoryRepository.update(inputCodeCategory);
  }

  /**
   * コード分類を一括更新します。
   *
   * @param codeCategories
   * @return
   */
  public int updateAll(final List<CodeCategory> codeCategories) {
    Assert.notNull(codeCategories, "inputCodeCategory must not be null");
    return codeCategoryRepository.updateAll(codeCategories);
  }

  /**
   * コード分類を削除します。
   *
   * @return
   */
  public CodeCategory delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeCategoryRepository.delete(id);
  }

  /**
   * コード分類を一括削除します。
   *
   * @param codeCategories
   * @return
   */
  public int deleteAll(final List<CodeCategory> codeCategories) {
    Assert.notNull(codeCategories, "inputCodeCategory must not be null");
    return codeCategoryRepository.deleteAll(codeCategories);
  }
}

package com.bigtreetc.sample.doma.domain.repository.system;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.boot.Pageables.toSelectOptions;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.dao.system.CodeDao;
import com.bigtreetc.sample.doma.domain.model.system.Code;
import com.bigtreetc.sample.doma.domain.model.system.CodeCriteria;
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

@RequiredArgsConstructor
@Repository
public class CodeRepository {

  @NonNull final CodeDao codeDao;

  /**
   * コードを全件取得します。
   *
   * @return
   */
  public List<Code> fetchAll() {
    val pageable = Pageable.unpaged();
    val options = toSelectOptions(pageable).count();
    return codeDao.selectAll(new CodeCriteria(), options, toList());
  }

  /**
   * コードを複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Code> findAll(CodeCriteria criteria, Pageable pageable) {
    val options = toSelectOptions(pageable).count();
    val data = codeDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * コードを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Code> findOne(CodeCriteria criteria) {
    return codeDao.select(criteria);
  }

  /**
   * コードを取得します。
   *
   * @return
   */
  public Code findById(final Long id) {
    return codeDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * コードを登録します。
   *
   * @param code
   * @return
   */
  public Code create(final Code code) {
    codeDao.insert(code);
    return code;
  }

  /**
   * コードを一括登録します。
   *
   * @param codes
   * @return
   */
  public int createAll(final List<Code> codes) {
    val inserted = codeDao.insert(codes);
    return Arrays.stream(inserted).sum();
  }

  /**
   * コードを更新します。
   *
   * @param code
   * @return
   */
  public Code update(final Code code) {
    int updated = codeDao.update(code);

    if (updated < 1) {
      throw new NoDataFoundException("code_id=" + code.getId() + " のデータが見つかりません。");
    }

    return code;
  }

  /**
   * コードを一括更新します。
   *
   * @param codes
   * @return
   */
  public int updateAll(final List<Code> codes) {
    val updated = codeDao.update(codes);
    return Arrays.stream(updated).sum();
  }

  /**
   * コードを削除します。
   *
   * @return
   */
  public Code delete(final Long id) {
    val code =
        codeDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));

    int updated = codeDao.delete(code);
    if (updated < 1) {
      throw new NoDataFoundException("code_id=" + id + " は更新できませんでした。");
    }

    return code;
  }

  /**
   * コードを一括削除します。
   *
   * @return
   */
  public int deleteAll(final List<Code> codes) {
    val deleted = codeDao.delete(codes);
    return Arrays.stream(deleted).sum();
  }
}

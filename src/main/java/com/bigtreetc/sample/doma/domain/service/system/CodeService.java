package com.bigtreetc.sample.doma.domain.service.system;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.model.system.Code;
import com.bigtreetc.sample.doma.domain.model.system.CodeCriteria;
import com.bigtreetc.sample.doma.domain.repository.system.CodeRepository;
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

@RequiredArgsConstructor
@Service
public class CodeService extends BaseTransactionalService {

  @NonNull final CodeRepository codeRepository;

  /**
   * コードを複数取得します。
   *
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<Code> findAll(CodeCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeRepository.findAll(criteria, pageable);
  }

  /**
   * コードを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Code> findOne(CodeCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeRepository.findOne(criteria);
  }

  /**
   * コードを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Code findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeRepository.fetchAll().stream()
        .filter(c -> c.getId() == id.longValue())
        .findFirst()
        .orElseThrow(() -> new NoDataFoundException("id=" + id + " のデータが見つかりません。"));
  }

  /**
   * コードを登録します。
   *
   * @param code
   * @return
   */
  public Code create(final Code code) {
    Assert.notNull(code, "code must not be null");
    return codeRepository.create(code);
  }

  /**
   * コードを一括登録します。
   *
   * @param codes
   * @return
   */
  public int createAll(final List<Code> codes) {
    Assert.notNull(codes, "codes must not be null");
    return codeRepository.createAll(codes);
  }

  /**
   * コードを更新します。
   *
   * @param code
   * @return
   */
  public Code update(final Code code) {
    Assert.notNull(code, "code must not be null");
    return codeRepository.update(code);
  }

  /**
   * コードを一括更新します。
   *
   * @param codes
   * @return
   */
  public int updateAll(final List<Code> codes) {
    Assert.notNull(codes, "code must not be null");
    return codeRepository.updateAll(codes);
  }

  /**
   * コードを削除します。
   *
   * @return
   */
  public Code delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeRepository.delete(id);
  }

  /**
   * コードを一括削除します。
   *
   * @param codes
   * @return
   */
  public int deleteAll(final List<Code> codes) {
    Assert.notNull(codes, "code must not be null");
    return codeRepository.deleteAll(codes);
  }
}

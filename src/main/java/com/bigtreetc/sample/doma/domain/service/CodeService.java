package com.bigtreetc.sample.doma.domain.service;

import com.bigtreetc.sample.doma.base.domain.service.BaseTransactionalService;
import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.base.util.CsvUtils;
import com.bigtreetc.sample.doma.domain.model.Code;
import com.bigtreetc.sample.doma.domain.model.CodeCriteria;
import com.bigtreetc.sample.doma.domain.repository.CodeRepository;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
   * コードマスタを検索します。
   *
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<Code> findAll(CodeCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeRepository.findAll(criteria, pageable);
  }

  /**
   * コードマスタを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Code> findOne(CodeCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeRepository.findOne(criteria);
  }

  /**
   * コードマスタを取得します。
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
   * コードマスタを登録します。
   *
   * @param code
   * @return
   */
  public Code create(final Code code) {
    Assert.notNull(code, "code must not be null");
    return codeRepository.create(code);
  }

  /**
   * コードマスタを一括登録します。
   *
   * @param codes
   * @return
   */
  public int createAll(final List<Code> codes) {
    Assert.notNull(codes, "codes must not be null");
    return codeRepository.createAll(codes);
  }

  /**
   * コードマスタを更新します。
   *
   * @param code
   * @return
   */
  public Code update(final Code code) {
    Assert.notNull(code, "code must not be null");
    return codeRepository.update(code);
  }

  /**
   * コードマスタを一括更新します。
   *
   * @param codes
   * @return
   */
  public int updateAll(final List<Code> codes) {
    Assert.notNull(codes, "code must not be null");
    return codeRepository.updateAll(codes);
  }

  /**
   * コードマスタを削除します。
   *
   * @return
   */
  public Code delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeRepository.delete(id);
  }

  /**
   * コードマスタを一括削除します。
   *
   * @param codes
   * @return
   */
  public int deleteAll(final List<Code> codes) {
    Assert.notNull(codes, "code must not be null");
    return codeRepository.deleteAll(codes);
  }

  /**
   * コードマスタを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(OutputStream outputStream, CodeCriteria criteria, Class<?> clazz)
      throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = codeRepository.findAll(criteria)) {
      CsvUtils.writeCsv(outputStream, clazz, data, code -> modelMapper.map(code, clazz));
    }
  }
}

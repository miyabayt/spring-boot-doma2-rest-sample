package com.bigtreetc.sample.doma.domain.service.system;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.model.system.MailTemplate;
import com.bigtreetc.sample.doma.domain.model.system.MailTemplateCriteria;
import com.bigtreetc.sample.doma.domain.repository.system.MailTemplateRepository;
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
public class MailTemplateService extends BaseTransactionalService {

  @NonNull final MailTemplateRepository mailTemplateRepository;

  /**
   * メールテンプレートを複数取得します。
   *
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<MailTemplate> findAll(MailTemplateCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return mailTemplateRepository.findAll(criteria, pageable);
  }

  /**
   * メールテンプレートを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<MailTemplate> findOne(MailTemplateCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return mailTemplateRepository.findOne(criteria);
  }

  /**
   * メールテンプレートを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public MailTemplate findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return mailTemplateRepository.fetchAll().stream()
        .filter(c -> c.getId() == id.longValue())
        .findFirst()
        .orElseThrow(() -> new NoDataFoundException("id=" + id + " のデータが見つかりません。"));
  }

  /**
   * メールテンプレートを登録します。
   *
   * @param mailTemplate
   * @return
   */
  public MailTemplate create(final MailTemplate mailTemplate) {
    Assert.notNull(mailTemplate, "mailTemplate must not be null");
    return mailTemplateRepository.create(mailTemplate);
  }

  /**
   * メールテンプレートを一括登録します。
   *
   * @param mailTemplates
   * @return
   */
  public int createAll(final List<MailTemplate> mailTemplates) {
    Assert.notNull(mailTemplates, "mailTemplates must not be null");
    return mailTemplateRepository.createAll(mailTemplates);
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param mailTemplate
   * @return
   */
  public MailTemplate update(final MailTemplate mailTemplate) {
    Assert.notNull(mailTemplate, "mailTemplate must not be null");
    return mailTemplateRepository.update(mailTemplate);
  }

  /**
   * メールテンプレートを一括更新します。
   *
   * @param mailTemplates
   * @return
   */
  public int updateAll(final List<MailTemplate> mailTemplates) {
    Assert.notNull(mailTemplates, "mailTemplate must not be null");
    return mailTemplateRepository.updateAll(mailTemplates);
  }

  /**
   * メールテンプレートを削除します。
   *
   * @return
   */
  public MailTemplate delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return mailTemplateRepository.delete(id);
  }

  /**
   * メールテンプレートを一括削除します。
   *
   * @param mailTemplates
   * @return
   */
  public int deleteAll(final List<MailTemplate> mailTemplates) {
    Assert.notNull(mailTemplates, "mailTemplate must not be null");
    return mailTemplateRepository.deleteAll(mailTemplates);
  }
}

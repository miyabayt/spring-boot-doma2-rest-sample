package com.bigtreetc.sample.doma.domain.repository;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.boot.Pageables.toSelectOptions;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.dao.MailTemplateDao;
import com.bigtreetc.sample.doma.domain.model.MailTemplate;
import com.bigtreetc.sample.doma.domain.model.MailTemplateCriteria;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MailTemplateRepository {

  @NonNull final MailTemplateDao mailTemplateDao;

  /**
   * メールテンプレートを全件取得します。
   *
   * @return
   */
  public List<MailTemplate> fetchAll() {
    val pageable = Pageable.unpaged();
    val options = toSelectOptions(pageable).count();
    return mailTemplateDao.selectAll(new MailTemplateCriteria(), options, toList());
  }

  /**
   * メールテンプレートを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<MailTemplate> findAll(MailTemplateCriteria criteria, Pageable pageable) {
    val options = toSelectOptions(pageable).count();
    val data = mailTemplateDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * メールテンプレートを検索します。
   *
   * @param criteria
   * @return
   */
  public Stream<MailTemplate> findAll(MailTemplateCriteria criteria) {
    return mailTemplateDao.selectAll(criteria);
  }

  /**
   * メールテンプレートを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<MailTemplate> findOne(MailTemplateCriteria criteria) {
    return mailTemplateDao.select(criteria);
  }

  /**
   * メールテンプレートを取得します。
   *
   * @return
   */
  public MailTemplate findById(final Long id) {
    return mailTemplateDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("mailTemplate_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * メールテンプレートを登録します。
   *
   * @param mailTemplate
   * @return
   */
  public MailTemplate create(final MailTemplate mailTemplate) {
    mailTemplateDao.insert(mailTemplate);
    return mailTemplate;
  }

  /**
   * メールテンプレートを一括登録します。
   *
   * @param mailTemplates
   * @return
   */
  public int createAll(final List<MailTemplate> mailTemplates) {
    val inserted = mailTemplateDao.insert(mailTemplates);
    return Arrays.stream(inserted).sum();
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param mailTemplate
   * @return
   */
  public MailTemplate update(final MailTemplate mailTemplate) {
    int updated = mailTemplateDao.update(mailTemplate);

    if (updated < 1) {
      throw new NoDataFoundException("mailTemplate_id=" + mailTemplate.getId() + " のデータが見つかりません。");
    }

    return mailTemplate;
  }

  /**
   * メールテンプレートを一括更新します。
   *
   * @param mailTemplates
   * @return
   */
  public int updateAll(final List<MailTemplate> mailTemplates) {
    val updated = mailTemplateDao.update(mailTemplates);
    return Arrays.stream(updated).sum();
  }

  /**
   * メールテンプレートを削除します。
   *
   * @return
   */
  public MailTemplate delete(final Long id) {
    val mailTemplate =
        mailTemplateDao
            .selectById(id)
            .orElseThrow(
                () -> new NoDataFoundException("mailTemplate_id=" + id + " のデータが見つかりません。"));

    int updated = mailTemplateDao.delete(mailTemplate);
    if (updated < 1) {
      throw new NoDataFoundException("mailTemplate_id=" + id + " は更新できませんでした。");
    }

    return mailTemplate;
  }

  /**
   * メールテンプレートを一括削除します。
   *
   * @return
   */
  public int deleteAll(final List<MailTemplate> mailTemplates) {
    val deleted = mailTemplateDao.delete(mailTemplates);
    return Arrays.stream(deleted).sum();
  }
}

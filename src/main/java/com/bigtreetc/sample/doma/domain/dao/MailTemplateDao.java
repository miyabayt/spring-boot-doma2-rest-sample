package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.MailTemplate;
import com.bigtreetc.sample.doma.domain.model.MailTemplateCriteria;
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
public interface MailTemplateDao {

  /**
   * メールテンプレートを検索します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final MailTemplateCriteria criteria,
      final SelectOptions options,
      final Collector<MailTemplate, ?, R> collector);

  /**
   * メールテンプレートを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<MailTemplate> selectAll(final MailTemplateCriteria criteria);

  /**
   * メールテンプレートを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<MailTemplate> selectById(Long id);

  /**
   * メールテンプレートを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<MailTemplate> select(MailTemplateCriteria criteria);

  /**
   * メールテンプレートを登録します。
   *
   * @param mailtemplate
   * @return
   */
  @Insert
  int insert(MailTemplate mailtemplate);

  /**
   * メールテンプレートを更新します。
   *
   * @param mailTemplate
   * @return
   */
  @Update
  int update(MailTemplate mailTemplate);

  /**
   * メールテンプレートを削除します。
   *
   * @param mailTemplate
   * @return
   */
  @Delete
  int delete(MailTemplate mailTemplate);

  /**
   * メールテンプレートを一括登録します。
   *
   * @param mailTemplates
   * @return
   */
  @BatchInsert
  int[] insert(List<MailTemplate> mailTemplates);

  /**
   * メールテンプレートを一括更新します。
   *
   * @param mailTemplates
   * @return
   */
  @BatchUpdate
  int[] update(List<MailTemplate> mailTemplates);

  /**
   * メールテンプレートを一括削除します。
   *
   * @param mailTemplates
   * @return
   */
  @BatchDelete
  int[] delete(List<MailTemplate> mailTemplates);
}

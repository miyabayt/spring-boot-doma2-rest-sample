package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.SendMailQueue;
import com.bigtreetc.sample.doma.domain.model.SendMailQueueCriteria;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.message.Message;

@ConfigAutowireable
@Dao
public interface SendMailQueueDao {

  /**
   * メール送信キューを検索します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final SendMailQueueCriteria criteria,
      final SelectOptions options,
      final Collector<SendMailQueue, ?, R> collector);

  /**
   * メール送信キューを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<SendMailQueue> selectAll(final SendMailQueueCriteria criteria);

  /**
   * メール送信キューを一括登録します。
   *
   * @param sendMailQueues
   * @return
   */
  @BatchInsert
  int[] insert(List<SendMailQueue> sendMailQueues);

  /**
   * メール送信キューを一括更新します。
   *
   * @param sendMailQueues
   * @return
   */
  @BatchUpdate
  int[] update(List<SendMailQueue> sendMailQueues);
}

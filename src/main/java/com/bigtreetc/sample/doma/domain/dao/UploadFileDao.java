package com.bigtreetc.sample.doma.domain.dao;

import com.bigtreetc.sample.doma.domain.model.*;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.message.Message;

@ConfigAutowireable
@Dao
public interface UploadFileDao {

  /**
   * アップロードファイルを検索します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      UploadFileCriteria criteria,
      SelectOptions options,
      final Collector<StaffRole, ?, R> collector);

  /**
   * アップロードファイルを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<UploadFile> selectAll(final UploadFileCriteria criteria);

  /**
   * アップロードファイルを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  UploadFile selectById(Long id);

  /**
   * アップロードファイルを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  UploadFile select(UploadFileCriteria criteria);

  /**
   * アップロードファイルを登録します。
   *
   * @param uploadFile
   * @return
   */
  @Insert
  int insert(UploadFile uploadFile);

  /**
   * アップロードファイルを更新します。
   *
   * @param uploadFile
   * @return
   */
  @Update
  int update(UploadFile uploadFile);

  /**
   * アップロードファイルを物理削除します。
   *
   * @param uploadFile
   * @return
   */
  @Update(excludeNull = true)
  int delete(UploadFile uploadFile);

  /**
   * アップロードファイルを一括登録します。
   *
   * @param uploadFiles
   * @return
   */
  @BatchInsert
  int[] insert(List<UploadFile> uploadFiles);
}

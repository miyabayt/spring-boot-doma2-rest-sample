package com.bigtreetc.sample.doma.domain.model;

import com.bigtreetc.sample.doma.base.domain.model.BaseEntityImpl;
import com.bigtreetc.sample.doma.base.domain.model.MultipartFileConvertible;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Getter
@Setter
@Table(name = "upload_files")
@Entity
public class UploadFile extends BaseEntityImpl implements MultipartFileConvertible {

  private static final long serialVersionUID = 1738092593334285554L;

  @JsonIgnore @OriginalStates // 差分UPDATEのために定義する
  UploadFile originalStates;

  @Id
  @Column(name = "upload_file_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // ファイル名
  @Column(name = "file_name")
  String filename;

  // オリジナルファイル名
  @Column(name = "original_file_name")
  String originalFilename;

  // コンテンツタイプ
  String contentType;

  // コンテンツ
  byte[] content;
}

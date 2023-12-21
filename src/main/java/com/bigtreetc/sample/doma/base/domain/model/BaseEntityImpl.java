package com.bigtreetc.sample.doma.base.domain.model;

import com.bigtreetc.sample.doma.base.domain.dao.DefaultEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Version;

@SuppressWarnings("serial")
@Entity(listener = DefaultEntityListener.class) // 自動的にシステム制御項目を更新するためにリスナーを指定する
@Setter
@Getter
public abstract class BaseEntityImpl implements BaseEntity, Serializable {

  // 作成者
  @Column(updatable = false)
  @JsonIgnore
  String createdBy;

  // 作成日時
  @Column(updatable = false)
  LocalDateTime createdAt;

  // 更新者
  @JsonIgnore String updatedBy;

  // 更新日時
  LocalDateTime updatedAt;

  // 楽観的排他制御で使用する改定番号
  @Version Integer version;
}

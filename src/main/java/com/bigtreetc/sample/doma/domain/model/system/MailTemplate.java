package com.bigtreetc.sample.doma.domain.model.system;

import com.bigtreetc.sample.doma.base.domain.model.BaseEntityImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "mail_templates")
@Entity
@Getter
@Setter
public class MailTemplate extends BaseEntityImpl {

  private static final long serialVersionUID = -2997823123579780864L;

  @JsonIgnore @OriginalStates // 差分UPDATEのために定義する
  MailTemplate originalStates;

  @Id
  @Column(name = "mail_template_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // カテゴリコード
  String categoryCode;

  // メールテンプレートコード
  String templateCode;

  // メールタイトル
  String subject;

  // メール本文
  String templateBody;
}

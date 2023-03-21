package com.bigtreetc.sample.doma.controller.mailtemplates;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchMailTemplateRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  // メールテンプレートコード
  String templateCode;

  // メールタイトル
  String subject;

  // メール本文
  String templateBody;
}

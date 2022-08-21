package com.bigtreetc.sample.doma.domain.model.system;

import com.bigtreetc.sample.doma.base.domain.model.BaseEntityImpl;
import com.bigtreetc.sample.doma.domain.model.common.CommaSeparatedString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "send_mail_queue")
@Entity
@Getter
@Setter
public class SendMailQueue extends BaseEntityImpl {

  private static final long serialVersionUID = -4135869799913706558L;

  @JsonIgnore @OriginalStates // 差分UPDATEのために定義する
  SendMailQueue originalStates;

  @Id
  @Column(name = "send_mail_queue_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "from_address")
  String from;

  @Column(name = "to_address")
  CommaSeparatedString to;

  @Column(name = "cc_address")
  CommaSeparatedString cc;

  @Column(name = "bcc_address")
  CommaSeparatedString bcc;

  LocalDateTime sentAt;

  String subject;

  String body;
}

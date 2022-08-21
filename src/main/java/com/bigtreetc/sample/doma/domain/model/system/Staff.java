package com.bigtreetc.sample.doma.domain.model.system;

import com.bigtreetc.sample.doma.base.domain.model.BaseEntityImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "staffs")
@Entity
@Getter
@Setter
public class Staff extends BaseEntityImpl {

  private static final long serialVersionUID = -3762941082070995608L;

  @JsonIgnore @OriginalStates // 差分UPDATEのために定義する
  Staff originalStates;

  @Id
  @Column(name = "staff_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @JsonIgnore String password;

  // 名前
  String firstName;

  // 苗字
  String lastName;

  // メールアドレス
  @Email String email;

  // 電話番号
  @Digits(fraction = 0, integer = 10)
  String tel;

  // パスワードリセットトークン
  @JsonIgnore String passwordResetToken;

  // トークン失効日
  LocalDateTime tokenExpiresAt;
}

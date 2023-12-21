package com.bigtreetc.sample.doma.domain.model;

import com.bigtreetc.sample.doma.base.domain.model.BaseEntityImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "users")
@Entity
@Getter
@Setter
public class User extends BaseEntityImpl {

  private static final long serialVersionUID = 4512633005852272922L;

  @JsonIgnore @OriginalStates // 差分UPDATEのために定義する
  User originalStates;

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // ハッシュ化されたパスワード
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

  // 郵便番号
  @NotEmpty String zip;

  // 住所
  @NotEmpty String address;

  // 添付ファイルID
  Long uploadFileId;
}

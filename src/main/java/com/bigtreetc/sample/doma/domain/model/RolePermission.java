package com.bigtreetc.sample.doma.domain.model;

import com.bigtreetc.sample.doma.base.domain.model.BaseEntityImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "role_permissions")
@Entity
@Getter
@Setter
public class RolePermission extends BaseEntityImpl {

  private static final long serialVersionUID = 4915898548766398327L;

  @JsonIgnore @OriginalStates // 差分UPDATEのために定義する
  RolePermission originalStates;

  @Id
  @Column(name = "role_permission_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // ロールコード
  String roleCode;

  // 権限コード
  String permissionCode;

  // 有効
  Boolean isEnabled;
}

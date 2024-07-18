package com.bigtreetc.sample.doma.domain.model;

import static com.bigtreetc.sample.doma.base.util.ValidateUtils.isEquals;

import com.bigtreetc.sample.doma.base.domain.model.BaseEntityImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "roles")
@Entity
@Getter
@Setter
public class Role extends BaseEntityImpl {

  @JsonIgnore @OriginalStates // 差分UPDATEのために定義する
  Role originalStates;

  @Id
  @Column(name = "role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // ロールコード
  String roleCode;

  // ロール名
  String roleName;

  final @Transient List<RolePermission> rolePermissions = new ArrayList<>();

  final @Transient List<Permission> permissions = new ArrayList<>();

  public boolean hasPermission(String permissionCode) {
    return rolePermissions.stream()
        .anyMatch(rp -> isEquals(rp.getPermissionCode(), permissionCode) && rp.getIsEnabled());
  }

  public void setPermission(String permissionCode, boolean isEnabled) {
    rolePermissions.stream()
        .filter(rp -> isEquals(rp.getPermissionCode(), permissionCode))
        .findFirst()
        .ifPresent(rp -> rp.setIsEnabled(isEnabled));
  }
}

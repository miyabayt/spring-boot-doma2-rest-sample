package com.bigtreetc.sample.doma.controller.system.permissions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PermissionRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  // 権限コード
  String permissionCode;

  // 権限名
  String permissionName;

  // 改定番号
  Integer version;
}

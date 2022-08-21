package com.bigtreetc.sample.doma.controller.system.roles;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchRoleRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  // ロールコード
  String roleCode;

  // ロール名
  String roleName;
}

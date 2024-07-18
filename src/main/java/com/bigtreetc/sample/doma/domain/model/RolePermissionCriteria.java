package com.bigtreetc.sample.doma.domain.model;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermissionCriteria extends RolePermission {

  Collection<String> roleCodes;
}

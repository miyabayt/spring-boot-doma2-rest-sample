package com.bigtreetc.sample.doma.security;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toSet;

import com.bigtreetc.sample.doma.domain.dao.RolePermissionDao;
import com.bigtreetc.sample.doma.domain.dao.StaffDao;
import com.bigtreetc.sample.doma.domain.dao.StaffRoleDao;
import com.bigtreetc.sample.doma.domain.model.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/** 管理側 認証認可 */
@RequiredArgsConstructor
@Component
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

  @NonNull final StaffDao staffDao;

  @NonNull final StaffRoleDao staffRoleDao;

  @NonNull final RolePermissionDao rolePermissionDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Staff staff = null;
    List<GrantedAuthority> authorityList = null;

    try {
      val criteria = new StaffCriteria();
      criteria.setEmail(username);

      // 担当者を取得して、セッションに保存する
      staff =
          staffDao
              .select(criteria)
              .orElseThrow(
                  () ->
                      new UsernameNotFoundException("no staff found [username=" + username + "]"));

      // 担当者権限を取得する
      val staffRoleCriteria = new StaffRoleCriteria();
      staffRoleCriteria.setStaffId(staff.getId());
      val staffRoles = staffRoleDao.selectAll(staffRoleCriteria, SelectOptions.get(), toList());

      // ロールコードにプレフィックスをつけてまとめる
      val roleCodes = staffRoles.stream().map(StaffRole::getRoleCode).collect(toSet());
      val rolePermissions = getRolePermissions(roleCodes);

      // 権限コードをまとめる
      val permissionCodes =
          rolePermissions.stream()
              .filter(RolePermission::getIsEnabled)
              .map(RolePermission::getPermissionCode)
              .collect(toSet());

      // ロールと権限を両方ともGrantedAuthorityとして渡す
      Set<String> authorities = new HashSet<>();
      for (val roleCode : roleCodes) {
        authorities.add("ROLE_%s".formatted(roleCode));
      }
      authorities.addAll(permissionCodes);
      authorityList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));

      return User.withUsername(staff.getId().toString())
          .password(staff.getPassword())
          .authorities(authorityList)
          .build();

    } catch (NoResultException e) {
      throw e;
    } catch (Exception e) {
      throw new UsernameNotFoundException("could not select account.", e);
    }
  }

  private List<RolePermission> getRolePermissions(Set<String> roleCodes) {
    val criteria = new RolePermissionCriteria();
    criteria.setRoleCodes(roleCodes);
    criteria.setIsEnabled(true);
    return rolePermissionDao.selectAll(criteria, SelectOptions.get(), toList());
  }
}

package com.bigtreetc.sample.doma.domain.repository;

import static com.bigtreetc.sample.doma.base.util.ValidateUtils.isNotEmpty;
import static java.util.stream.Collectors.toList;
import static org.seasar.doma.boot.Pageables.toSelectOptions;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.dao.RoleDao;
import com.bigtreetc.sample.doma.domain.dao.RolePermissionDao;
import com.bigtreetc.sample.doma.domain.model.Role;
import com.bigtreetc.sample.doma.domain.model.RoleCriteria;
import com.bigtreetc.sample.doma.domain.model.RolePermission;
import com.bigtreetc.sample.doma.domain.model.RolePermissionCriteria;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** ロールリポジトリ */
@RequiredArgsConstructor
@Repository
public class RoleRepository {

  @NonNull final RoleDao roleDao;

  @NonNull final RolePermissionDao rolePermissionDao;

  /**
   * ロールを複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Role> findAll(RoleCriteria criteria, Pageable pageable) {
    val options = toSelectOptions(pageable).count();
    val data = roleDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * ロールを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Role> findOne(RoleCriteria criteria) {
    val role = roleDao.select(criteria);

    role.ifPresent(
        r -> {
          val rolePermissions = findRolePermissions(r);
          if (isNotEmpty(rolePermissions)) {
            r.getRolePermissions().addAll(rolePermissions);
          }
        });

    return role;
  }

  /**
   * ロールを取得します。
   *
   * @return
   */
  public Role findById(final Long id) {
    val role =
        roleDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("role_id=" + id + " のデータが見つかりません。"));

    val rolePermissions = findRolePermissions(role);
    if (isNotEmpty(rolePermissions)) {
      role.getRolePermissions().addAll(rolePermissions);
    }

    return role;
  }

  /**
   * ロールを登録します。
   *
   * @param inputRole
   * @return
   */
  public Role create(final Role inputRole) {
    roleDao.insert(inputRole);

    // ロール権限紐付けを登録する
    val rolePermissions = inputRole.getRolePermissions();
    rolePermissionDao.insert(rolePermissions);

    return inputRole;
  }

  /**
   * ロールを一括登録します。
   *
   * @param roles
   * @return
   */
  public int createAll(final List<Role> roles) {
    val inserted = roleDao.insert(roles);
    return Arrays.stream(inserted).sum();
  }

  /**
   * ロールを更新します。
   *
   * @param inputRole
   * @return
   */
  public Role update(final Role inputRole) {
    int updated = roleDao.update(inputRole);

    if (updated < 1) {
      throw new NoDataFoundException("role_id=" + inputRole.getId() + " のデータが見つかりません。");
    }

    // ロール権限紐付けを更新する
    val rolePermissions = inputRole.getRolePermissions();
    for (val rp : rolePermissions) {
      rolePermissionDao.update(rp);
    }

    return inputRole;
  }

  /**
   * ロールを一括更新します。
   *
   * @param roles
   * @return
   */
  public int updateAll(final List<Role> roles) {
    val updated = roleDao.update(roles);
    return Arrays.stream(updated).sum();
  }

  /**
   * ロールを削除します。
   *
   * @return
   */
  public Role delete(final Long id) {
    val role =
        roleDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("role_id=" + id + " のデータが見つかりません。"));

    int updated = roleDao.delete(role);
    if (updated < 1) {
      throw new NoDataFoundException("role_id=" + id + " は更新できませんでした。");
    }

    // ロール権限紐付けを削除する
    deleteRolePermissions(role);

    return role;
  }

  /**
   * ロールを一括削除します。
   *
   * @return
   */
  public int deleteAll(final List<Role> roles) {
    val deleted = roleDao.delete(roles);
    return Arrays.stream(deleted).sum();
  }

  /**
   * ロール権限紐付けを削除する
   *
   * @param inputRole
   */
  protected void deleteRolePermissions(final Role inputRole) {
    List<RolePermission> rolePermissionsToDelete = findRolePermissions(inputRole);

    if (isNotEmpty(rolePermissionsToDelete)) {
      rolePermissionDao.delete(rolePermissionsToDelete);
    }
  }

  /**
   * ロール権限紐付けを取得する
   *
   * @param inputRole
   * @return
   */
  protected List<RolePermission> findRolePermissions(Role inputRole) {
    // ロール権限紐付けをロールロールで取得する
    val criteria = new RolePermissionCriteria();
    criteria.setRoleCode(inputRole.getRoleCode());

    val options = toSelectOptions(Pageable.unpaged());
    return rolePermissionDao.selectAll(criteria, options, toList());
  }
}

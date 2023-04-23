package com.bigtreetc.sample.doma.domain.repository;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.boot.Pageables.toSelectOptions;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.dao.PermissionDao;
import com.bigtreetc.sample.doma.domain.model.Permission;
import com.bigtreetc.sample.doma.domain.model.PermissionCriteria;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** 権限リポジトリ */
@RequiredArgsConstructor
@Repository
public class PermissionRepository {

  @NonNull final PermissionDao permissionDao;

  /**
   * 権限マスタを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Permission> findAll(PermissionCriteria criteria, Pageable pageable) {
    val options = toSelectOptions(pageable).count();
    val data = permissionDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * 権限マスタを検索します。
   *
   * @param criteria
   * @return
   */
  public Stream<Permission> findAll(PermissionCriteria criteria) {
    return permissionDao.selectAll(criteria);
  }

  /**
   * 権限マスタを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Permission> findOne(PermissionCriteria criteria) {
    return permissionDao.select(criteria);
  }

  /**
   * 権限マスタを取得します。
   *
   * @return
   */
  public Permission findById(final Long id) {
    return permissionDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * 権限マスタを登録します。
   *
   * @param inputPermission
   * @return
   */
  public Permission create(final Permission inputPermission) {
    permissionDao.insert(inputPermission);
    return inputPermission;
  }

  /**
   * 権限マスタを一括登録します。
   *
   * @param permissions
   * @return
   */
  public int createAll(final List<Permission> permissions) {
    val inserted = permissionDao.insert(permissions);
    return Arrays.stream(inserted).sum();
  }

  /**
   * 権限マスタを更新します。
   *
   * @param inputPermission
   * @return
   */
  public Permission update(final Permission inputPermission) {
    int updated = permissionDao.update(inputPermission);

    if (updated < 1) {
      throw new NoDataFoundException("permission_id=" + inputPermission.getId() + " のデータが見つかりません。");
    }

    return inputPermission;
  }

  /**
   * 権限マスタを一括更新します。
   *
   * @param permissions
   * @return
   */
  public int updateAll(final List<Permission> permissions) {
    val updated = permissionDao.update(permissions);
    return Arrays.stream(updated).sum();
  }

  /**
   * 権限マスタを削除します。
   *
   * @return
   */
  public Permission delete(final Long id) {
    val permission =
        permissionDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " のデータが見つかりません。"));

    int updated = permissionDao.delete(permission);
    if (updated < 1) {
      throw new NoDataFoundException("permission_id=" + id + " は更新できませんでした。");
    }

    return permission;
  }

  /**
   * 権限マスタを一括削除します。
   *
   * @return
   */
  public int deleteAll(final List<Permission> permissions) {
    val deleted = permissionDao.delete(permissions);
    return Arrays.stream(deleted).sum();
  }
}

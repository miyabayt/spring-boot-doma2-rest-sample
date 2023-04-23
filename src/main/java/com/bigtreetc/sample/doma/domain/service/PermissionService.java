package com.bigtreetc.sample.doma.domain.service;

import com.bigtreetc.sample.doma.base.domain.service.BaseTransactionalService;
import com.bigtreetc.sample.doma.base.util.CsvUtils;
import com.bigtreetc.sample.doma.domain.model.Permission;
import com.bigtreetc.sample.doma.domain.model.PermissionCriteria;
import com.bigtreetc.sample.doma.domain.repository.PermissionRepository;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/** 権限サービス */
@RequiredArgsConstructor
@Service
public class PermissionService extends BaseTransactionalService {

  @NonNull final PermissionRepository permissionRepository;

  /**
   * 権限マスタを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<Permission> findAll(PermissionCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return permissionRepository.findAll(criteria, pageable);
  }

  /**
   * 権限マスタを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Permission> findOne(PermissionCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return permissionRepository.findOne(criteria);
  }

  /**
   * 権限マスタを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Permission findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return permissionRepository.findById(id);
  }

  /**
   * 権限マスタを登録します。
   *
   * @param inputPermission
   * @return
   */
  public Permission create(final Permission inputPermission) {
    Assert.notNull(inputPermission, "inputPermission must not be null");
    return permissionRepository.create(inputPermission);
  }

  /**
   * 権限マスタを一括登録します。
   *
   * @param permissions
   * @return
   */
  public int createAll(final List<Permission> permissions) {
    Assert.notNull(permissions, "permissions must not be null");
    return permissionRepository.createAll(permissions);
  }

  /**
   * 権限マスタを更新します。
   *
   * @param inputPermission
   * @return
   */
  public Permission update(final Permission inputPermission) {
    Assert.notNull(inputPermission, "inputPermission must not be null");
    return permissionRepository.update(inputPermission);
  }

  /**
   * 権限マスタを一括更新します。
   *
   * @param permissions
   * @return
   */
  public int updateAll(final List<Permission> permissions) {
    Assert.notNull(permissions, "permission must not be null");
    return permissionRepository.updateAll(permissions);
  }

  /**
   * 権限マスタを削除します。
   *
   * @return
   */
  public Permission delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return permissionRepository.delete(id);
  }

  /**
   * 権限マスタを一括削除します。
   *
   * @param permissions
   * @return
   */
  public int deleteAll(final List<Permission> permissions) {
    Assert.notNull(permissions, "permission must not be null");
    return permissionRepository.deleteAll(permissions);
  }

  /**
   * 権限マスタを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(
      OutputStream outputStream, PermissionCriteria criteria, Class<?> clazz) throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = permissionRepository.findAll(criteria)) {
      CsvUtils.writeCsv(
          outputStream, clazz, data, permission -> modelMapper.map(permission, clazz));
    }
  }
}

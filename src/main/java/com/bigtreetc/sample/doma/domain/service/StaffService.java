package com.bigtreetc.sample.doma.domain.service;

import com.bigtreetc.sample.doma.base.domain.service.BaseTransactionalService;
import com.bigtreetc.sample.doma.base.util.CsvUtils;
import com.bigtreetc.sample.doma.domain.model.Staff;
import com.bigtreetc.sample.doma.domain.model.StaffCriteria;
import com.bigtreetc.sample.doma.domain.repository.StaffRepository;
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

/** 担当者サービス */
@RequiredArgsConstructor
@Service
public class StaffService extends BaseTransactionalService {

  @NonNull final StaffRepository staffRepository;

  /**
   * 担当者マスタを検索します。
   *
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<Staff> findAll(StaffCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return staffRepository.findAll(criteria, pageable);
  }

  /**
   * 担当者マスタを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Staff> findOne(StaffCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return staffRepository.findOne(criteria);
  }

  /**
   * 担当者マスタを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Staff findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return staffRepository.findById(id);
  }

  /**
   * 担当者マスタを登録します。
   *
   * @param staff
   * @return
   */
  public Staff create(final Staff staff) {
    Assert.notNull(staff, "staff must not be null");
    return staffRepository.create(staff);
  }

  /**
   * 担当者マスタを一括登録します。
   *
   * @param staffs
   * @return
   */
  public int createAll(final List<Staff> staffs) {
    Assert.notNull(staffs, "staffs must not be null");
    return staffRepository.createAll(staffs);
  }

  /**
   * 担当者マスタを更新します。
   *
   * @param inputStaff
   * @return
   */
  public Staff update(final Staff inputStaff) {
    Assert.notNull(inputStaff, "staff must not be null");
    val staff = staffRepository.findById(inputStaff.getId());
    modelMapper.map(inputStaff, staff);
    return staffRepository.update(staff);
  }

  /**
   * 担当者マスタを一括更新します。
   *
   * @param staffs
   * @return
   */
  public int updateAll(final List<Staff> staffs) {
    Assert.notNull(staffs, "staff must not be null");
    return staffRepository.updateAll(staffs);
  }

  /**
   * 担当者マスタを削除します。
   *
   * @return
   */
  public Staff delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return staffRepository.delete(id);
  }

  /**
   * 担当者マスタを一括削除します。
   *
   * @param staffs
   * @return
   */
  public int deleteAll(final List<Staff> staffs) {
    Assert.notNull(staffs, "staff must not be null");
    return staffRepository.deleteAll(staffs);
  }

  /**
   * 担当者マスタを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(OutputStream outputStream, StaffCriteria criteria, Class<?> clazz)
      throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = staffRepository.findAll(criteria)) {
      CsvUtils.writeCsv(outputStream, clazz, data, staff -> modelMapper.map(staff, clazz));
    }
  }
}

package com.bigtreetc.sample.doma.domain.repository;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.boot.Pageables.toSelectOptions;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.dao.RolePermissionDao;
import com.bigtreetc.sample.doma.domain.dao.StaffDao;
import com.bigtreetc.sample.doma.domain.dao.StaffRoleDao;
import com.bigtreetc.sample.doma.domain.model.Staff;
import com.bigtreetc.sample.doma.domain.model.StaffCriteria;
import com.bigtreetc.sample.doma.domain.model.StaffRole;
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

/** 担当者リポジトリ */
@RequiredArgsConstructor
@Repository
public class StaffRepository {

  @NonNull final StaffDao staffDao;

  @NonNull final StaffRoleDao staffRoleDao;

  @NonNull final RolePermissionDao rolePermissionDao;

  /**
   * 担当者を複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Staff> findAll(StaffCriteria criteria, Pageable pageable) {
    val options = toSelectOptions(pageable).count();
    val data = staffDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * 担当者を取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Staff> findOne(StaffCriteria criteria) {
    return staffDao.select(criteria);
  }

  /**
   * 担当者を取得します。
   *
   * @return
   */
  public Staff findById(final Long id) {
    return staffDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * 担当者を登録します。
   *
   * @param staff
   * @return
   */
  public Staff create(final Staff staff) {
    staffDao.insert(staff);

    // ロール権限紐付けを登録する
    val staffRole = new StaffRole();
    staffRole.setStaffId(staff.getId());
    staffRole.setRoleCode("admin");
    staffRoleDao.insert(staffRole);

    return staff;
  }

  /**
   * 担当者を一括登録します。
   *
   * @param staffs
   * @return
   */
  public int createAll(final List<Staff> staffs) {
    val inserted = staffDao.insert(staffs);
    return Arrays.stream(inserted).sum();
  }

  /**
   * 担当者を更新します。
   *
   * @param inputStaff
   * @return
   */
  public Staff update(final Staff inputStaff) {
    int updated = staffDao.update(inputStaff);

    if (updated < 1) {
      throw new NoDataFoundException("staff_id=" + inputStaff.getId() + " のデータが見つかりません。");
    }

    return inputStaff;
  }

  /**
   * 担当者を一括更新します。
   *
   * @param staffs
   * @return
   */
  public int updateAll(final List<Staff> staffs) {
    val updated = staffDao.update(staffs);
    return Arrays.stream(updated).sum();
  }

  /**
   * 担当者を削除します。
   *
   * @return
   */
  public Staff delete(final Long id) {
    val staff =
        staffDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " のデータが見つかりません。"));

    int updated = staffDao.delete(staff);
    if (updated < 1) {
      throw new NoDataFoundException("staff_id=" + id + " は更新できませんでした。");
    }

    return staff;
  }

  /**
   * 担当者を一括削除します。
   *
   * @return
   */
  public int deleteAll(final List<Staff> staffs) {
    val deleted = staffDao.delete(staffs);
    return Arrays.stream(deleted).sum();
  }
}

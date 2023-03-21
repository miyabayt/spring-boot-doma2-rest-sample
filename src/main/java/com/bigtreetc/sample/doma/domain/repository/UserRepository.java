package com.bigtreetc.sample.doma.domain.repository;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.boot.Pageables.toSelectOptions;

import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.dao.UploadFileDao;
import com.bigtreetc.sample.doma.domain.dao.UserDao;
import com.bigtreetc.sample.doma.domain.dao.UserRoleDao;
import com.bigtreetc.sample.doma.domain.model.User;
import com.bigtreetc.sample.doma.domain.model.UserCriteria;
import com.bigtreetc.sample.doma.domain.model.UserRole;
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

/** ユーザリポジトリ */
@RequiredArgsConstructor
@Repository
public class UserRepository {

  @NonNull final UserDao userDao;

  @NonNull final UserRoleDao userRoleDao;

  @NonNull final UploadFileDao uploadFileDao;

  /**
   * ユーザを取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
    val options = toSelectOptions(pageable).count();
    val data = userDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * ユーザを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<User> findOne(UserCriteria criteria) {
    return userDao.select(criteria);
  }

  /**
   * ユーザ取得します。
   *
   * @return
   */
  public User findById(final Long id) {
    return userDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("user_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * ユーザを登録します。
   *
   * @param inputUser
   * @return
   */
  public User create(final User inputUser) {
    userDao.insert(inputUser);

    // ロール権限紐付けを登録する
    val userRole = new UserRole();
    userRole.setUserId(inputUser.getId());
    userRole.setRoleCode("user");
    userRoleDao.insert(userRole);

    return inputUser;
  }

  /**
   * ユーザを一括登録します。
   *
   * @param users
   * @return
   */
  public int createAll(final List<User> users) {
    val inserted = userDao.insert(users);
    return Arrays.stream(inserted).sum();
  }

  /**
   * ユーザを更新します。
   *
   * @param inputUser
   * @return
   */
  public User update(final User inputUser) {
    int updated = userDao.update(inputUser);
    if (updated < 1) {
      throw new NoDataFoundException("user_id=" + inputUser.getId() + " のデータが見つかりません。");
    }

    return inputUser;
  }

  /**
   * ユーザを一括更新します。
   *
   * @param users
   * @return
   */
  public int updateAll(final List<User> users) {
    val updated = userDao.update(users);
    return Arrays.stream(updated).sum();
  }

  /**
   * ユーザを削除します。
   *
   * @return
   */
  public User delete(final Long id) {
    val user =
        userDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("user_id=" + id + " のデータが見つかりません。"));

    int updated = userDao.delete(user);
    if (updated < 1) {
      throw new NoDataFoundException("user_id=" + id + " は更新できませんでした。");
    }

    return user;
  }

  /**
   * ユーザを一括削除します。
   *
   * @return
   */
  public int deleteAll(final List<User> users) {
    val deleted = userDao.delete(users);
    return Arrays.stream(deleted).sum();
  }
}

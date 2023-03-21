package com.bigtreetc.sample.doma.domain.service;

import com.bigtreetc.sample.doma.base.domain.service.BaseTransactionalService;
import com.bigtreetc.sample.doma.domain.model.User;
import com.bigtreetc.sample.doma.domain.model.UserCriteria;
import com.bigtreetc.sample.doma.domain.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
public class UserService extends BaseTransactionalService {

  @NonNull final UserRepository userRepository;

  /**
   * ユーザを複数取得します。
   *
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return userRepository.findAll(criteria, pageable);
  }

  /**
   * ユーザを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<User> findOne(UserCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return userRepository.findOne(criteria);
  }

  /**
   * ユーザを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public User findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return userRepository.findById(id);
  }

  /**
   * ユーザを登録します。
   *
   * @param user
   * @return
   */
  public User create(final User user) {
    Assert.notNull(user, "user must not be null");
    return userRepository.create(user);
  }

  /**
   * ユーザを一括登録します。
   *
   * @param users
   * @return
   */
  public int createAll(final List<User> users) {
    Assert.notNull(users, "users must not be null");
    return userRepository.createAll(users);
  }

  /**
   * ユーザを更新します。
   *
   * @param user
   * @return
   */
  public User update(final User user) {
    Assert.notNull(user, "user must not be null");
    return userRepository.update(user);
  }

  /**
   * ユーザを一括更新します。
   *
   * @param users
   * @return
   */
  public int updateAll(final List<User> users) {
    Assert.notNull(users, "user must not be null");
    return userRepository.updateAll(users);
  }

  /**
   * ユーザを削除します。
   *
   * @return
   */
  public User delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return userRepository.delete(id);
  }

  /**
   * ユーザを一括削除します。
   *
   * @param users
   * @return
   */
  public int deleteAll(final List<User> users) {
    Assert.notNull(users, "user must not be null");
    return userRepository.deleteAll(users);
  }
}

package com.bigtreetc.sample.doma.base.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudRepository<T, C extends T> {

  Page<T> findAll(C criteria, Pageable pageable);

  Optional<T> findOne(C criteria);

  T findById(Long id);

  T create(T dto);

  int createAll(List<T> list);

  T update(T dto);

  int updateAll(List<T> list);

  T delete(Long id);

  int deleteAll(List<T> list);
}

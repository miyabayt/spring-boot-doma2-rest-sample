package com.bigtreetc.sample.doma.base.domain.dao;

import com.bigtreetc.sample.doma.base.domain.model.BaseEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

@NoArgsConstructor // コンストラクタが必須のため
@Slf4j
public class DefaultEntityListener<ENTITY> implements EntityListener<ENTITY> {

  @Override
  public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
    if (entity instanceof BaseEntity baseEntity) {
      val createdAt = AuditInfoHolder.getAuditDateTime();
      val createdBy = AuditInfoHolder.getAuditUser();
      baseEntity.setCreatedAt(createdAt); // 作成日
      baseEntity.setCreatedBy(createdBy); // 作成者
    }
  }

  @Override
  public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {
    if (entity instanceof BaseEntity baseEntity) {
      val updatedAt = AuditInfoHolder.getAuditDateTime();
      val updatedBy = AuditInfoHolder.getAuditUser();
      baseEntity.setUpdatedAt(updatedAt); // 更新日
      baseEntity.setUpdatedBy(updatedBy); // 更新者
    }
  }
}

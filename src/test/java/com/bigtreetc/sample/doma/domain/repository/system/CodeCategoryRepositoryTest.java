package com.bigtreetc.sample.doma.domain.repository.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.bigtreetc.sample.doma.BaseTestContainerTest;
import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.model.system.CodeCategoryCriteria;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeCategoryRepositoryTest extends BaseTestContainerTest {

  @Autowired CodeCategoryRepository codeCategoryRepository;

  @Test
  @DisplayName("リポジトリがNULLではないこと")
  void test1() {
    assertThat(codeCategoryRepository).isNotNull();
  }

  @Test
  @DisplayName("指定した分類コードのレコードが取得できること")
  void test2() {
    val expectedCategoryCode = "target";
    val criteria = new CodeCategoryCriteria();
    criteria.setCategoryCode(expectedCategoryCode);

    val found =
        codeCategoryRepository
            .findOne(criteria)
            .orElseThrow(
                () ->
                    new NoDataFoundException(
                        "codeCategory_code=" + expectedCategoryCode + " のデータが見つかりません。"));

    assertThat(found).isNotNull();
    assertThat(found.getCategoryCode()).isEqualTo(expectedCategoryCode);
  }
}

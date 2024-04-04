package com.bigtreetc.sample.doma.domain.repository.system;

import static org.assertj.core.api.Assertions.assertThat;

import com.bigtreetc.sample.doma.BaseTestContainerTest;
import com.bigtreetc.sample.doma.base.exception.NoDataFoundException;
import com.bigtreetc.sample.doma.domain.model.CodeCriteria;
import com.bigtreetc.sample.doma.domain.repository.CodeRepository;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeRepositoryTest extends BaseTestContainerTest {

  @Autowired CodeRepository codeRepository;

  @Test
  @DisplayName("リポジトリがNULLではないこと")
  void test1() {
    assertThat(codeRepository).isNotNull();
  }

  @Test
  @DisplayName("指定した分類コードのレコードが取得できること")
  void test2() {
    val expectedCategoryCode = "target";
    val criteria = new CodeCriteria();
    criteria.setCategoryCode(expectedCategoryCode);

    val found =
        codeRepository
            .findOne(criteria)
            .orElseThrow(
                () ->
                    new NoDataFoundException(
                        "category_code=" + expectedCategoryCode + " のデータが見つかりません。"));

    assertThat(found).isNotNull();
    assertThat(found.getCategoryCode()).isEqualTo(expectedCategoryCode);
  }
}

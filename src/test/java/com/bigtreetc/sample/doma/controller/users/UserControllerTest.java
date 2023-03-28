package com.bigtreetc.sample.doma.controller.users;

import static com.bigtreetc.sample.doma.base.web.BaseWebConst.ACCESS_DENIED_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigtreetc.sample.doma.BaseTestContainerTest;
import com.bigtreetc.sample.doma.base.util.MessageUtils;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class UserControllerTest extends BaseTestContainerTest {

  @Autowired WebApplicationContext wac;

  MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
  }

  @Test
  @DisplayName("権限を持つロールで、顧客マスタを検索できること")
  @WithMockUser(authorities = "user:read")
  void test1() throws Exception {
    mockMvc
        .perform(get("/api/user/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value("true"))
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  @DisplayName("権限を持たないロールでは、顧客マスタを検索できないこと")
  @WithAnonymousUser
  void test2() throws Exception {
    val message = MessageUtils.getMessage(ACCESS_DENIED_ERROR);

    mockMvc
        .perform(get("/api/user/users"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.message").value(message));
  }
}

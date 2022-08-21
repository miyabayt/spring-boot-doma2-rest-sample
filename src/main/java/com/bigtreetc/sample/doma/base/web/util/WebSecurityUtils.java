package com.bigtreetc.sample.doma.base.web.util;

import java.util.Collection;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class WebSecurityUtils {

  private static final SpelParserConfiguration config = new SpelParserConfiguration(true, true);

  private static final SpelExpressionParser parser = new SpelExpressionParser(config);

  /**
   * 認証済みかどうかを示す値を返します。
   *
   * @return
   */
  public static boolean isAuthenticated() {
    val auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.isAuthenticated();
  }

  /**
   * 引数に指定した権限を持っているかどうかを示す値を返します。
   *
   * @param a
   * @return
   */
  public static boolean hasAuthority(final String a) {
    val authorities = getAuthorities();

    Boolean isAllowed = false;
    for (GrantedAuthority ga : authorities) {
      val authority = ga.getAuthority();
      val expressionString = String.format("'%s' matches '%s'", a, authority);
      val expression = parser.parseExpression(expressionString);

      isAllowed = expression.getValue(Boolean.class);
      log.debug("{} matches {}, result={}", a, authority, isAllowed);

      if (isAllowed != null && isAllowed) {
        break;
      }
    }

    if (isAllowed == null) {
      isAllowed = false;
    }

    return isAllowed;
  }

  /**
   * 引数に指定したロールを持っているかどうかを示す値を返します。
   *
   * @param role
   * @return
   */
  public static boolean hasRole(final String role) {
    return getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
  }

  /**
   * 認証に使用されるIDを返します。
   *
   * @return
   */
  public static Optional<String> getPrincipal() {
    val authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      val principal = authentication.getPrincipal();

      if (principal instanceof String) {
        return Optional.of((String) principal);
      }
    }

    return Optional.empty();
  }

  /**
   * 保有している権限を返します。
   *
   * @return
   */
  public static Collection<? extends GrantedAuthority> getAuthorities() {
    val auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities();
  }
}

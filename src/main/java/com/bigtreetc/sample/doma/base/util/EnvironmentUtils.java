package com.bigtreetc.sample.doma.base.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

@Slf4j
public class EnvironmentUtils {

  private static boolean isTestEnv = false;

  private static boolean isLocalOrTestEnv = false;

  public static void init(Environment environment) {
    Predicate<String> isLocalPredicate = profile -> Objects.equals(profile, "local");
    Predicate<String> isTestPredicate = profile -> Objects.equals(profile, "test");

    boolean isTest = false;
    boolean isLocalOrTest;
    if (environment.getActiveProfiles().length > 0) {
      isTest = Arrays.stream(environment.getActiveProfiles()).anyMatch(isTestPredicate);
      isLocalOrTest =
          Arrays.stream(environment.getActiveProfiles())
              .anyMatch(isLocalPredicate.or(isTestPredicate));
    } else {
      isLocalOrTest = true;
    }

    log.info("isTestEnv: {}", isTest);
    EnvironmentUtils.isTestEnv = isTest;

    log.info("isLocalOrTest: {}", isLocalOrTest);
    EnvironmentUtils.isLocalOrTestEnv = isLocalOrTest;
  }

  public static boolean isTest() {
    return EnvironmentUtils.isTestEnv;
  }

  public static boolean isLocalOrTest() {
    return EnvironmentUtils.isLocalOrTestEnv;
  }
}

package com.bigtreetc.sample.doma.base.util;

import org.slf4j.MDC;

public class MDCUtils {

  public static final String MDC_TRACK_ID = "TRACK_ID";

  public static final String MDC_USER_ID = "USER_ID";

  /**
   * MDCの値を設定します。
   *
   * @param key
   * @param value
   */
  public static void put(String key, String value) {
    MDC.put(key, value);
  }

  /**
   * 未設定の場合のみMDCの値を設定します。
   *
   * @param key
   * @param value
   */
  public static void putIfAbsent(String key, String value) {
    if (MDC.get(key) == null) MDC.put(key, value);
  }

  /**
   * MDCの値を削除します。
   *
   * @param key
   */
  public static void remove(String key) {
    MDC.remove(key);
  }

  /** MDCをクリアします。 */
  public static void clear() {
    MDC.clear();
  }
}

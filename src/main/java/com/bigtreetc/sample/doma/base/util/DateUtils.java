package com.bigtreetc.sample.doma.base.util;

import static com.bigtreetc.sample.doma.base.util.ValidateUtils.isNotEmpty;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/** 日付ユーティリティ */
@Slf4j
public class DateUtils {

  public static final String SYSTEM_PROPERTY_DATE = "SYSTEM_DATE_OVERRIDE";

  public static final String SYSTEM_PROPERTY_DATETIME = "SYSTEM_DATETIME_OVERRIDE";

  /**
   * 現在時刻を返します。
   *
   * @return
   */
  public static LocalDateTime now() {
    val currentDateTimeString = System.getProperty(SYSTEM_PROPERTY_DATETIME);
    if (isNotEmpty(currentDateTimeString)) {
      return LocalDateTime.parse(currentDateTimeString);
    }
    val currentDateString = System.getProperty(SYSTEM_PROPERTY_DATE);
    if (isNotEmpty(currentDateString)) {
      val date = LocalDate.parse(currentDateString);
      return LocalDateTime.of(date, LocalTime.now());
    }
    return LocalDateTime.now();
  }

  /**
   * Date型の値を指定されたDateTimeFormatterフォーマットした値を返します。
   *
   * @param fromDate
   * @param formatter
   * @return
   */
  public static String format(final Date fromDate, final DateTimeFormatter formatter) {
    val zoneId = getZoneId();
    val localDateTime = fromDate.toInstant().atZone(zoneId).toLocalDateTime();
    return formatter.format(localDateTime);
  }

  /**
   * LocalDateTime型の値を指定されたDateTimeFormatterフォーマットした値を返します。
   *
   * @param fromLocalDateTime
   * @param formatter
   * @return
   */
  public static String format(
      final LocalDateTime fromLocalDateTime, final DateTimeFormatter formatter) {
    return formatter.format(fromLocalDateTime);
  }

  /**
   * Date型の値をLocalDateTime型に変換して返します。
   *
   * @param fromDate
   * @return
   */
  public static LocalDateTime toLocalDateTime(final Date fromDate) {
    val zoneId = getZoneId();
    return fromDate.toInstant().atZone(zoneId).toLocalDateTime();
  }

  /**
   * LocalDateTime型の値をDate型に変換して返します。
   *
   * @param fromLocalDateTime
   * @return
   */
  public static Date toDate(final LocalDateTime fromLocalDateTime) {
    val zoneId = getZoneId();
    val zoneDateTime = ZonedDateTime.of(fromLocalDateTime, zoneId);
    return Date.from(zoneDateTime.toInstant());
  }

  /**
   * LocalDate型の値をDate型に変換して返します。
   *
   * @param localDate
   * @return
   */
  public static Date toDate(final LocalDate localDate) {
    val zoneId = getZoneId();
    val zoneDateTime = localDate.atStartOfDay(zoneId).toInstant();
    return Date.from(zoneDateTime);
  }

  protected static ZoneId getZoneId() {
    return ZoneId.systemDefault();
  }
}

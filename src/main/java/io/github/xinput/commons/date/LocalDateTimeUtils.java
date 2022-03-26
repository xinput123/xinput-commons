package io.github.xinput.commons.date;

import org.apache.commons.lang3.ObjectUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author xinput
 * LocalDateTime 工具类
 */
public class LocalDateTimeUtils {

  // ========================= LocalDateTime => 指定类型字符串 ======================

  /**
   * LocalDateTime -> yyyyMMddHHmmss
   */
  public static String formatTimeStamp(LocalDateTime localDateTime) {
    return format(localDateTime, DateUtils.DATE_TIME_TIMESTAMP_FORMATTER);
  }

  /**
   * LocalDateTime -> yyyy-MM-dd HH:mm:ss
   */
  public static String formatDateTime(LocalDateTime localDateTime) {
    return format(localDateTime, DateUtils.DATE_TIME_FORMATTER);
  }

  /**
   * LocalDateTime -> yyyy-MM-dd
   */
  public static String formatDate(LocalDateTime localDateTime) {
    return format(localDateTime, DateUtils.DATE_FORMATTER);
  }

  /**
   * LocalDateTime -> yyyyMMdd
   */
  public static String formatDay(LocalDateTime localDateTime) {
    return format(localDateTime, DateUtils.DAY_FORMATTER);
  }

  /**
   * LocalDate -> MM-dd
   */
  public static String formatMonthDate(LocalDateTime localDateTime) {
    return format(localDateTime, DateUtils.MONTH_DATE_FORMATTER);
  }

  /**
   * LocalDateTime -> HH:mm:ss
   */
  public static String formatTime(LocalDateTime localDateTime) {
    return format(localDateTime, DateUtils.TIME_FORMATTER);
  }

  /**
   * LocalDateTime -> 自定义格式
   */
  public static String format(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
    return localDateTime.format(dateTimeFormatter);
  }

  // ========================= 指定类型字符串 => LocalDateTime ======================

  /**
   * yyyyMMddHHmmss -> LocalDateTime
   */
  public static LocalDateTime parseTimeStamp(String dateString) {
    return parse(dateString, DateUtils.DATE_TIME_TIMESTAMP_FORMATTER);
  }

  /**
   * yyyy-MM-dd HH:mm:ss -> LocalDateTime
   */
  public static LocalDateTime parse(String dateString) {
    return parse(dateString, DateUtils.DATE_TIME_FORMATTER);
  }

  /**
   * yyyy-MM-dd HH:mm:ss -> LocalDateTime
   */
  public static LocalDateTime parse(String dateString, DateTimeFormatter dateTimeFormatter) {
    return LocalDateTime.parse(dateString, dateTimeFormatter);
  }

  // ===============================================

  /**
   * Date -> LocalDateTime
   */
  public static LocalDateTime as(Date date) {
    try {
      return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * LocalDate -> LocalDateTime
   */
  public static LocalDateTime as(LocalDate localDate) {
    return LocalDateTime.of(localDate, LocalTime.parse("00:00:00"));
  }

  /**
   * 时间戳(毫秒) -> LocalDateTime
   */
  public static LocalDateTime asMillSecond(Long millSecond) {
    if (millSecond == null) {
      return null;
    }
    return Instant.ofEpochMilli(millSecond).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * 时间戳(秒) -> LocalDateTime
   */
  public static LocalDateTime asSecond(Long second) {
    if (second == null) {
      return null;
    }
    return Instant.ofEpochSecond(second).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * 判断 date 是否在 (beginDate,endDate) 之间
   */
  public static boolean between(LocalDateTime date, LocalDateTime beginDate, LocalDateTime endDate) {
    if (!ObjectUtils.allNotNull(date, beginDate, endDate)) {
      return false;
    }

    if (!beginDate.isBefore(endDate)) {
      return false;
    }

    return beginDate.isBefore(date) && endDate.isAfter(date);
  }

  /**
   * 判断 date 是否在 [beginDate,endDate) 之间,左闭右开
   */
  public static boolean in(LocalDateTime date, LocalDateTime beginDate, LocalDateTime endDate) {
    if (!ObjectUtils.allNotNull(date, beginDate, endDate)) {
      return false;
    }

    if (!beginDate.isBefore(endDate)) {
      return false;
    }

    return date.isEqual(beginDate) || beginDate.isBefore(date) && endDate.isAfter(date);
  }

  /**
   * 获取指定时间对应的时间戳:毫秒
   */
  public static long getMillis(LocalDateTime localDateTime) {
    return localDateTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
  }

  /**
   * 获取当前时间对应的时间戳:毫秒
   */
  public static long getMillis() {
    return getMillis(LocalDateTime.now());
  }

  /**
   * 获取指定时间对应的时间戳:秒
   */
  public static long getSecond(LocalDateTime localDateTime) {
    return localDateTime.toInstant(OffsetDateTime.now().getOffset()).getEpochSecond();
  }

  /**
   * 获取当前时间对应的时间戳:秒
   */
  public static long getSecond() {
    return getSecond(LocalDateTime.now());
  }
}

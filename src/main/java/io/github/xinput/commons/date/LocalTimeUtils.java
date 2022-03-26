package io.github.xinput.commons.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author xinput
 * LocalTime 工具类
 */
public class LocalTimeUtils {

  /**
   * LocalDateTime -> LocalTime
   */
  public static LocalTime as(LocalDateTime localDateTime) {
    return localDateTime.toLocalTime();
  }

  /**
   * Date -> LocalTime
   */
  public static LocalTime as(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

  /**
   * 时间戳(毫秒) -> LocalTime
   */
  public static LocalTime asMillSecond(Long millSecond) {
    if (millSecond == null) {
      return null;
    }
    return Instant.ofEpochMilli(millSecond).atZone(ZoneId.systemDefault()).toLocalTime();
  }

  /**
   * 时间戳(秒) -> LocalTime
   */
  public static LocalTime asSecond(Long second) {
    if (second == null) {
      return null;
    }
    return Instant.ofEpochSecond(second).atZone(ZoneId.systemDefault()).toLocalTime();
  }

  /**
   * LocalTime -> HH:mm:ss
   */
  public static String formatTime(LocalTime localTime) {
    return format(localTime, DateUtils.TIME_FORMATTER);
  }

  /**
   * LocalDateTime -> 自定义格式
   */
  public static String format(LocalTime localTime, DateTimeFormatter dateTimeFormatter) {
    return localTime.format(dateTimeFormatter);
  }

  /**
   * String -> LocalTime
   */
  public static LocalTime parseTime(String dateString) {
    return parseTime(dateString, DateUtils.TIME_FORMATTER);
  }

  /**
   * 自定义格式 -> LocalTime
   */
  public static LocalTime parseTime(String dateString, DateTimeFormatter dateTimeFormatter) {
    return LocalTime.parse(dateString, dateTimeFormatter);
  }
}

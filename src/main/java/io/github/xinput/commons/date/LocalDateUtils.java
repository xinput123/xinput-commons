package io.github.xinput.commons.date;

import com.google.common.collect.Lists;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xinput
 * @date 2020-07-31 13:56
 */
public class LocalDateUtils {

  /**
   * LocalDate -> yyyy-MM-dd
   */
  public static String formatDate(LocalDate localDate) {
    return format(localDate, DateUtils.DATE_FORMATTER);
  }

  /**
   * LocalDate -> yyyyMMdd
   */
  public static String formatDay(LocalDate localDate) {
    return format(localDate, DateUtils.DAY_FORMATTER);
  }

  /**
   * LocalDate -> 自定义格式
   */
  public static String format(LocalDate localDate, DateTimeFormatter dateTimeFormatter) {
    return localDate.format(dateTimeFormatter);
  }

  /**
   * LocalDate -> MM-dd
   */
  public static String formatMonthDate(LocalDate localDate) {
    return format(localDate, DateUtils.MONTH_DATE_FORMATTER);
  }

  /**
   * yyyy-MM-dd -> LocalDate
   */
  public static LocalDate parseDate(String dateString) {
    return LocalDate.parse(dateString, DateUtils.DATE_FORMATTER);
  }

  /**
   * 自定义格式 -> LocalDate
   */
  public static LocalDate parse(String dateString, DateTimeFormatter dateTimeFormatter) {
    return LocalDate.parse(dateString, dateTimeFormatter);
  }

  /**
   * Date -> LocalDate
   */
  public static LocalDate as(Date date) {
    return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
  }

  /**
   * LocalDateTime -> LocalDate
   */
  public static LocalDate as(LocalDateTime localDateTime) {
    return localDateTime.toLocalDate();
  }

  /**
   * 时间戳(毫秒) -> LocalDateTime
   */
  public static LocalDate asMillSecond(Long millSecond) {
    if (millSecond == null) {
      return null;
    }
    return Instant.ofEpochMilli(millSecond).atZone(ZoneId.systemDefault()).toLocalDate();
  }

  /**
   * 时间戳(秒) -> LocalDateTime
   */
  public static LocalDate asSecond(Long second) {
    if (second == null) {
      return null;
    }

    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(second), ZoneId.systemDefault());
    return as(localDateTime);
  }

  /**
   * 计算两个日期相隔天数
   */
  public static int differentDays(LocalDate localDate1, LocalDate localDate2) {
    if (localDate1 == null || localDate2 == null) {
      throw new RuntimeException("日期不能为空");
    }

    /**
     * Period 用于计算时间间隔
     */
    return Period.between(localDate1, localDate2).getDays();
  }

  /**
   * 获取从其实日期到结束日期之间所有的日期
   *
   * @param beginDate 起始日期
   * @param endDate   结束日期
   * @return
   */
  public static List<LocalDate> getMiddleLocalDate(LocalDate beginDate, LocalDate endDate) {
    if (endDate.isBefore(beginDate)) {
      return Lists.newArrayList();
    }
    List<LocalDate> localDateList = new ArrayList<>();
    long length = endDate.toEpochDay() - beginDate.toEpochDay();
    for (long i = length; i >= 0; i--) {
      localDateList.add(endDate.minusDays(i));
    }
    return localDateList;
  }

  /**
   * 判断date1是否在date2之前
   */
  public static boolean isBeofre(LocalDate date1, LocalDate date2) {
    if (date1 == null) {
      return true;
    }

    if (date2 == null) {
      return false;
    }

    return date1.isBefore(date2);
  }

  /**
   * 判断date1是否在date2之后
   */
  public static boolean isAfter(LocalDate date1, LocalDate date2) {
    if (date1 == null) {
      return false;
    }

    if (date2 == null) {
      return true;
    }

    return date1.isAfter(date2);
  }

  /**
   * 判断 date 是否在 (beginDate,endDate) 之间
   */
  public static boolean between(LocalDate date, LocalDate beginDate, LocalDate endDate) {
    if (date == null || beginDate == null || endDate == null) {
      return false;
    }
    return beginDate.isBefore(date) && endDate.isAfter(date);
  }

  /**
   * 判断两个日期是否相等
   *
   * @param date1
   * @param date2
   * @return
   */
  public static boolean isEqual(LocalDate date1, LocalDate date2) {
    if (date1 == null && date2 == null) {
      return true;
    }

    if (date1 != null && date2 != null) {
      return date1.isEqual(date2);
    }

    return false;
  }

  /**
   * 根据出生日期计算年龄
   *
   * @param birthday
   * @return
   */
  public static Integer calculateAge(LocalDate birthday) {
    if (birthday == null) {
      return null;
    }

    return birthday.until(LocalDate.now()).getYears();
  }

  public static void main(String[] args) {
    long second = 678380400L;
    System.out.println(asSecond(second).toString());

    second = 1614614400000L;
    System.out.println(asMillSecond(second).toString());
    LocalDate ss = Instant.ofEpochSecond(678380400).atZone(ZoneId.systemDefault()).toLocalDate();
    System.out.println(ss.toString());
  }
}

package io.github.xinput.commons.date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author xinput
 * Date工具类 工具类
 */
public class DateUtils extends DateFormatUtils {

  private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

  public static final String DATE_TIME_TIMESTAMP_FORMATTER_STRING = "yyyyMMddHHmmss";

  public static final String DATE_TIME_FORMATTER_STRING = "yyyy-MM-dd HH:mm:ss";

  public static final String DATE_FORMATTER_STRING = "yyyy-MM-dd";

  public static final String DAY_FORMATTER_STRING = "yyyyMMdd";

  public static final String MONTH_DATE_FORMATTER_STRING = "MM-dd";

  public static final String TIME_FORMATTER_STRING = "HH:mm:ss";

  public static final DateTimeFormatter DATE_TIME_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_TIMESTAMP_FORMATTER_STRING);

  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_STRING);

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMATTER_STRING);

  public static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern(DAY_FORMATTER_STRING);

  public static final DateTimeFormatter MONTH_DATE_FORMATTER = DateTimeFormatter.ofPattern(MONTH_DATE_FORMATTER_STRING);

  public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMATTER_STRING);

  /**
   * LocalDateTime -> Date
   */
  public static Date as(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * LocalDate -> Date
   */
  public static Date as(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * Date -> yyyy-MM-dd HH:mm:ss
   */
  public static String formatDateTime(Date date) {
    return format(date, DATE_TIME_FORMATTER_STRING);
  }

  /**
   * Date -> yyyy-MM-dd
   */
  public static String formatDate(Date date) {
    return format(date, DATE_FORMATTER_STRING);
  }

  /**
   * Date -> yyyyMMdd
   */
  public static String formatDay(Date date) {
    return format(date, DAY_FORMATTER_STRING);
  }

  /**
   * Date -> HH:mm:ss
   */
  public static String formatTime(Date date) {
    return format(date, TIME_FORMATTER_STRING);
  }

  /**
   * 按照指定格式返回
   */
  public static String format(Date date, String pattern) {
    if (null == date) {
      return StringUtils.EMPTY;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    try {
      return sdf.format(date);
    } catch (Exception e) {
      logger.error("format date error. pattern:{}.", pattern, e);
      return StringUtils.EMPTY;
    }
  }

  /**
   * yyyy-MM-dd HH:mm:ss -> Date
   */
  public static Date parseDateTime(String source) {
    return parse(source, DATE_TIME_FORMATTER_STRING);
  }

  /**
   * yyyy-MM-dd -> Date
   */
  public static Date parseDate(String source) {
    return parse(source, DATE_FORMATTER_STRING);
  }

  /**
   * HH:mm:ss -> Date
   */
  public static Date parseTime(String source) {
    return parse(source, TIME_FORMATTER_STRING);
  }

  /**
   * 按照指定格式将字符串转为Date
   */
  public static Date parse(String source, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    Date date = null;
    try {
      return sdf.parse(source);
    } catch (ParseException e) {
      logger.error("parse date error. pattern:{}.", pattern, e);
    }

    return date;
  }

  /**
   * 计算date1和date2隔了多少天
   *
   * @param date1
   * @param date2
   * @return 如果返回值为0，表示同一天，如果大于0，表示date2在date1之后多少天，如果小于0，表示date2在date1之前多少天
   */
  public static int differentDays(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      throw new RuntimeException("日期不能为空");
    }
    LocalDate localDate1 = LocalDateUtils.as(date1);
    LocalDate localDate2 = LocalDateUtils.as(date2);

    /**
     * Period 用于计算时间间隔
     */
    return Period.between(localDate1, localDate2).getDays();
  }

  /**
   * 计算date1和date2之间隔了多少天，只有非负数
   *
   * @param date1
   * @param date2
   */
  public static int betweenDays(Date date1, Date date2) {
    return Math.abs(differentDays(date1, date2));
  }

}

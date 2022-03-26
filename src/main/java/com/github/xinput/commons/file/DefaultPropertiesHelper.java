package com.github.xinput.commons.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.xinput.commons.JsonHelper;
import com.github.xinput.commons.SimpleProperties;
import com.github.xinput.commons.StringHelper;
import com.github.xinput.commons.date.DateUtils;
import com.github.xinput.commons.date.LocalDateTimeUtils;
import com.github.xinput.commons.date.LocalDateUtils;
import com.github.xinput.commons.date.LocalTimeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 读取默认配置文件 - default.properties
 *
 * @author yuan.lai
 * @since
 */
public class DefaultPropertiesHelper {

  private static final Logger logger = LoggerFactory.getLogger(DefaultPropertiesHelper.class);

  /**
   * 默认配置文件名称
   */
  public static final String DEFAULT_SYSTEM_FILE = "default.properties";

  public static SimpleProperties SP;

  static {
    try {
      SP = SimpleProperties.readConfiguration(DEFAULT_SYSTEM_FILE);
    } catch (Exception e) {
      SP = null;
    }
  }

  /**
   * 获取自定义key对应的value
   */
  public static final String getString(String key) {
    return getString(key, null);
  }

  public static final String getString(String key, String defaultValue) {
    if (SP == null) {
      return defaultValue;
    }

    return SP.getStringProperty(key, defaultValue);
  }

  /**
   * 读取配置 - Int
   */
  public static final Integer getInt(String key) {
    return getInt(key, 0);
  }

  public static final int getInt(String key, Integer defaultValue) {
    String readValue = getString(key);
    if (StringHelper.isBlank(readValue)) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(readValue);
    } catch (Exception e) {
      logger.error("getInt error. key:{}, value:{}.", key, readValue, e);
      return defaultValue;
    }
  }

  /**
   * 读取配置 - Float
   */
  public static float getFloat(String key) {
    return getFloat(key, 0F);
  }

  public static float getFloat(String key, float defaultValue) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return defaultValue;
    }

    try {
      return Float.parseFloat(readValue);
    } catch (Exception e) {
      logger.error("getFloat error. key:{}, value:{}.", key, readValue, e);
      return defaultValue;
    }
  }

  /**
   * 读取配置 - Long
   */
  public static long getLong(String key) {
    return getLong(key, 0L);
  }

  /**
   * 读取配置 - Long
   *
   * @param defaultValue 默认值
   */
  public static long getLong(String key, long defaultValue) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return defaultValue;
    }

    try {
      return Long.parseLong(readValue);
    } catch (Exception e) {
      logger.error("getLong error. key:{}, value:{}.", key, readValue, e);
      return defaultValue;
    }
  }

  /**
   * 读取配置 - Double
   */
  public static double getDouble(String key) {
    return getDouble(key, 0D);
  }

  /**
   * 读取配置 - Double
   *
   * @param defaultValue 默认值
   */
  public static double getDouble(String key, double defaultValue) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return defaultValue;
    }

    try {
      return Double.parseDouble(readValue);
    } catch (Exception e) {
      logger.error("getDouble error. key:{}, value:{}.", key, readValue, e);
      return defaultValue;
    }
  }

  /**
   * 读取配置 - boolean
   */
  public static boolean getBoolean(String key) {
    return getBoolean(key, false);
  }

  /**
   * 读取配置 - boolean
   *
   * @param defaultValue 默认值
   */
  public static boolean getBoolean(String key, boolean defaultValue) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return defaultValue;
    }

    try {
      return Boolean.parseBoolean(readValue);
    } catch (Exception e) {
      logger.error("getBoolean error. key:{}, value:{}.", key, readValue, e);
      return defaultValue;
    }
  }

  /**
   * 读取配置 - Date
   */
  public static Date getDate(String key) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue) || StringUtils.equals(readValue, "0")) {
      return new Date();
    }

    try {
      return DateUtils.parse(readValue, DateUtils.DATE_TIME_FORMATTER_STRING);
    } catch (Exception e) {
      logger.error("getDate error. key:{}, readValue:{}.", key, readValue, e);
      return new Date();
    }
  }

  /**
   * 读取配置 - LocalDate
   */
  public static LocalDate getLocalDate(String key) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue) || StringUtils.equals(readValue, "0")) {
      return LocalDate.now();
    }

    try {
      return LocalDateUtils.parse(readValue, DateUtils.DATE_FORMATTER);
    } catch (Exception e) {
      logger.error("getLocalDate error. key:{}.", key, e);
      return LocalDate.now();
    }
  }

  /**
   * 读取配置 - LocalTime
   */
  public static LocalTime getLocalTime(String key) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue) || StringUtils.equals(readValue, "0")) {
      return LocalTime.now();
    }

    try {
      return LocalTimeUtils.parseTime(readValue, DateUtils.TIME_FORMATTER);
    } catch (Exception e) {
      logger.error("getLocalTime error. key:{}, readValue:{}.", key, readValue, e);
      return LocalTime.now();
    }
  }

  /**
   * 读取配置 - LocalDateTime
   */
  public static LocalDateTime getLocalDateTime(String key) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue) || StringUtils.equals(readValue, "0")) {
      return LocalDateTime.now();
    }

    try {
      return LocalDateTimeUtils.parse(readValue, DateUtils.DATE_TIME_FORMATTER);
    } catch (Exception e) {
      logger.error("getLocalDateTime error1. key:{},value:{}.", key, readValue, e);
      return LocalDateTime.now();
    }
  }

  /**
   * 读取配置 - 普通对象
   */
  public static <T> T getBean(String key,
                              Class<T> tClass) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return null;
    }

    try {
      return JsonHelper.toBean(readValue, tClass);
    } catch (Exception e) {
      logger.error("getBean error. key:{}, value:{}.", key, readValue, e);
      return null;
    }
  }

  /**
   * 读取配置 - 普通对象集合
   */
  public static List getList(String key) {
    return getList(key, String.class);
  }


  public static <T> List<T> getList(String key,
                                    Class<T> tClass) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return Lists.newArrayList();
    }

    try {
      return JsonHelper.toList(readValue, tClass);
    } catch (Exception e) {
      logger.error("getList error. key:{}, value:{}.", key, readValue, e);
      return Lists.newArrayList();
    }
  }

  public static <T> List<T> getList(String key,
                                    String defalutValue,
                                    Class<T> tClass) {
    String readValue = getString(key);
    if (StringUtils.isEmpty(readValue)) {
      readValue = defalutValue;
    }

    try {
      return JsonHelper.toList(readValue, tClass);
    } catch (Exception e) {
      logger.error("getList error. key:{}, value:{}.", key, readValue, e);
      return Lists.newArrayList();
    }
  }

  public static <V> Map<String, V> toMap(String key,
                                         Class<V> clazz) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return Maps.newHashMap();
    }

    try {
      return JsonHelper.toMap(readValue, clazz);
    } catch (Exception e) {
      logger.error("toMap error. key:{}, value:{}.", key, readValue, e);
      return Maps.newHashMap();
    }
  }

  public static <K, V> Map<K, V> toMap(String key,
                                       Class<K> keyClazz,
                                       Class<V> valueClazz) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return Maps.newHashMap();
    }

    try {
      return JsonHelper.toMap(readValue, keyClazz, valueClazz);
    } catch (Exception e) {
      logger.error("toMap error. key:{}, value:{}.", key, readValue, e);
      return Maps.newHashMap();
    }
  }

  public static <T> T parseObject(String key, TypeReference<T> typeReference) {
    String readValue = getString(key);
    if (StringUtils.isBlank(readValue)) {
      return null;
    }
    try {
      return JsonHelper.parseObject(readValue, typeReference);
    } catch (Exception e) {
      logger.error("parseObject exception. key:{}, value:{}. ", key, readValue, e);
      return null;
    }
  }
}

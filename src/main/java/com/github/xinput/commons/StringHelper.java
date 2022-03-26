package com.github.xinput.commons;

import org.apache.commons.lang3.StringUtils;

/**
 * String 工具类
 *
 * @author yuan.lai
 * @since
 */
public class StringHelper extends StringUtils {

  public static final String POINT = ".";

  public static final String SEPARATOR = "&";

  public static final String COMMA = ",";

  public static final String COLON = ":";

  public static final String SEMICOLON = ";";

  public static final String SLASH = "/";

  public static final char TAB = '\t';

  public static final int PHONE_LENGTH = 11;

  public static final int IDCARD_15_LENGTH = 15;

  public static final int IDCARD_18_LENGTH = 18;

  public static final String EXAMPLE_PASSWORD = "******";

  /**
   * null的字符串
   */
  public static final String NULL_STRING = "null";

  /**
   * 判断字符串是否为空, "null"这里也认为是空
   */
  public static boolean isNullOrEmpty(String value) {
    if (isEmpty(value)) {
      return true;
    }

    if (NULL_STRING.equalsIgnoreCase(value)) {
      return true;
    }

    return false;
  }

  /**
   * 判断字符串是否不为空
   */
  public static boolean isNotNullOrEmpty(String value) {
    return !isNullOrEmpty(value);
  }

  public static boolean notEquals(final CharSequence cs1, final CharSequence cs2) {
    return !equals(cs1, cs2);
  }

  public static boolean notEqualsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
    return !equalsIgnoreCase(cs1, cs2);
  }
}

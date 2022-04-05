package com.github.xinput.commons;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

public class ObjectHelper extends ObjectUtils {

  /**
   * 判断 obj 是否为 null
   */
  public static boolean isNull(Object obj) {
    return Objects.isNull(obj);
  }

  /**
   * 任意一个为null
   */
  public static boolean anyNull(Object... objs) {
    if (isNull(objs)) {
      return true;
    }

    for (Object obj : objs) {
      if (isNull(obj)) {
        return true;
      }
    }

    return false;
  }

  /**
   * 任意一个为null
   */
  public static boolean allNull(Object... objs) {
    if (isNull(objs)) {
      return true;
    }

    for (Object obj : objs) {
      if (!isNull(obj)) {
        return false;
      }
    }

    return true;
  }
}

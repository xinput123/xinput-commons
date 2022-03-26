package com.github.xinput.commons;

/**
 * 校验
 */
public class ValidHelper {

  /**
   * 手机号格式校验正则,目前手机号的号段开启太多，所有直接用最简单方式
   */
  public static final String PHONE_REGEX = "^1\\d{10}$";

  /**
   * 手机号格式校验
   *
   * @return true 合法、false不合法
   */
  public static final boolean phone(String phone) {
    if (StringHelper.isEmpty(phone)) {
      return false;
    }
    return phone.matches(PHONE_REGEX);
  }

  /**
   * 身份证号格式校验: 这里只简单的校验长度
   *
   * @return true 合法、false不合法
   */
  public static final boolean idCard(String idCard) {
    if (StringHelper.isEmpty(idCard)) {
      return false;
    }

    int idCardLength = idCard.length();
    return StringHelper.IDCARD_15_LENGTH == idCardLength || StringHelper.IDCARD_18_LENGTH == idCardLength;
  }
}

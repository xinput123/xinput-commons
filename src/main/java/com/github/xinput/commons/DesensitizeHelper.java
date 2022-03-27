package com.github.xinput.commons;

/**
 * 数据脱敏
 *
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 */
public class DesensitizeHelper {

  /**
   * 手机号脱敏筛选正则
   */
  public static final String PHONE_BLUR_REGEX = "(\\d{3})\\d{4}(\\d{4})";

  /**
   * 手机号脱敏替换正则
   */
  public static final String PHONE_BLUR_REPLACE_REGEX = "$1****$2";

  /**
   * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
   */
  public static String name(String fullName) {
    if (StringHelper.isBlank(fullName)) {
      return StringHelper.EMPTY;
    }
    String name = StringHelper.left(fullName, 1);
    return StringHelper.rightPad(name, StringHelper.length(fullName), "*");
  }

  /**
   * 脱敏手机号码
   */
  public static final String phone(String phone) {
    boolean checkFlag = ValidHelper.phone(phone);
    if (!checkFlag) {
      return phone;
    }
    return phone.replaceAll(PHONE_BLUR_REGEX, PHONE_BLUR_REPLACE_REGEX);
  }

  /**
   * 脱敏身份证号码
   * 15位：前六位，后三位
   * 18位：前六位，后四位
   */
  public static final String idCard(String idCard) {
    if (StringHelper.isBlank(idCard)) {
      return StringHelper.EMPTY;
    }

    String newIdCard = StringHelper.EMPTY;
    int length = idCard.length();
    switch (length) {
      case StringHelper.IDCARD_15_LENGTH:
        newIdCard = StringHelper.left(idCard, 6).concat(StringHelper.removeStart(StringHelper.leftPad(StringHelper.right(idCard, 3), StringHelper.length(idCard), "*"), "******"));
        break;
      case StringHelper.IDCARD_18_LENGTH:
        newIdCard = StringHelper.left(idCard, 6).concat(StringHelper.removeStart(StringHelper.leftPad(StringHelper.right(idCard, 4), StringHelper.length(idCard), "*"), "******"));
        break;
      default:
        newIdCard = idCard;
        break;
    }
    return newIdCard;
  }

  /**
   * 【银行卡号】前六位，后四位，其他用星号隐藏每位1个星号，比如：6222600**********1234
   */
  public static String bankCard(String cardNum) {
    if (StringHelper.isBlank(cardNum)) {
      return StringHelper.EMPTY;
    }
    return StringHelper.left(cardNum, 6).concat(StringHelper.removeStart(StringHelper.leftPad(StringHelper.right(cardNum, 4), StringHelper.length(cardNum), "*"), "******"));
  }

  /**
   * 【密码】密码的全部字符都用六个*代替，******
   */
  public static String password() {
    return StringHelper.EXAMPLE_PASSWORD;
  }
}

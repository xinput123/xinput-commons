package com.github.xinput.commons.sign;

import com.github.xinput.commons.StringHelper;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 加密算法
 */
public class SecureHelper {

  private static final Logger logger = LoggerFactory.getLogger(SecureHelper.class);

  /**
   * MD5 签名字符串
   *
   * @param message 需要签名的字符串,默认UTF-8
   */
  public static String MD5(String message) {
    return MD5(message, "utf-8");
  }

  /**
   * MD5 签名字符串
   *
   * @param message     需要签名的字符串
   * @param charsetName 编码格式(默认UTF-8)
   */
  public static String MD5(String message, String charsetName) {
    if (StringHelper.isNullOrEmpty(charsetName)) {
      charsetName = "utf-8";
    }
    try {
      return DigestUtils.md5Hex(message.getBytes(charsetName));
    } catch (UnsupportedEncodingException e) {
      logger.error("create MD5 exception. ", e);
    }

    return null;
  }

  /**
   * 生成 HMACSHA256
   *
   * @param message 待处理数据
   * @param key     密钥
   * @return 加密结果
   * @throws Exception
   */
  public static String HMACSHA256(String message, String key) {
    return HMACSHA256(message, key, "UTF-8");
  }

  /**
   * 生成 HMACSHA256
   *
   * @param message 待处理数据
   * @param key     密钥
   * @return 加密结果
   * @throws Exception
   */
  public static String HMACSHA256(String message, String key, String charsetName) {
    try {
      Mac sha256 = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      sha256.init(secretKeySpec);
      byte[] bytes = sha256.doFinal(message.getBytes(StandardCharsets.UTF_8));
      return Hex.encodeHexString(bytes).toUpperCase();
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      logger.error("create HMACSHA256 exception. ", e);
    }

    return null;
  }

  /**
   * 验证MD5签名字符串
   *
   * @param text 需要签名的字符串
   * @param sign 签名结果
   * @return 签名结果
   */
  public static boolean verifyMD5(String text, String sign) {
    return verifyMD5(text, sign, "utf-8");
  }

  /**
   * 验证MD5签名字符串
   *
   * @param text        需要签名的字符串
   * @param sign        签名结果
   * @param charsetName 编码格式
   * @return 签名结果
   */
  public static boolean verifyMD5(String text, String sign, String charsetName) {
    if (StringHelper.isNullOrEmpty(charsetName)) {
      charsetName = "utf-8";
    }

    if (StringHelper.equalsIgnoreCase(sign, MD5(text, charsetName))) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 验证HMACSHA256签名字符串
   *
   * @param text 需要签名的字符串
   * @param sign 签名结果
   * @return 签名结果
   */
  public static boolean verifyHMACSHA256(String text, String sign) {
    return verifyHMACSHA256(text, sign, "utf-8");
  }

  /**
   * 验证HMACSHA256签名字符串
   *
   * @param message     需要签名的字符串
   * @param sign        签名结果
   * @param charsetName 编码格式
   * @return 签名结果
   */
  public static boolean verifyHMACSHA256(String message, String sign, String charsetName) {
    if (StringHelper.isNullOrEmpty(charsetName)) {
      charsetName = "utf-8";
    }

    if (StringHelper.equalsIgnoreCase(sign, HMACSHA256(message, charsetName))) {
      return true;
    } else {
      return false;
    }
  }
}

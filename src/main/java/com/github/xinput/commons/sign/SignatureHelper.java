package com.github.xinput.commons.sign;

import com.github.xinput.commons.StringHelper;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.net.URLEncoder.encode;

/**
 * API signature demo.
 * 代码参考 https://github.com/fit2cloud/qingcloud-api-java-wrapper/blob/master/src/main/java/com/fit2cloud/qingcloud/wsclient/QingCloudWSClient.java
 */
public class SignatureHelper {
  public static final String DEFAULT_ENCODING = "UTF-8";

  private final static String VERSION = "1";

  // 1 year
  private final static int DEFAULT_EXPIRE = 10;

  private final static String ALGORITHM_HMACSHA256 = "HmacSHA256";

  /**
   * 计算签名
   *
   * @param secretKey
   * @param httpMethod
   * @param path
   * @param parameters
   * @return
   */
  public static String computeSignature(String secretKey, String httpMethod, String path,
                                        Map<String, String> parameters) {
    String[] sortedKeys = parameters.keySet().toArray(new String[]{});
    Arrays.sort(sortedKeys);

    StringBuilder sbStringToSign = new StringBuilder();
    sbStringToSign.append(httpMethod).append("\n").append(path).append("\n");

    String signature = StringHelper.EMPTY;
    try {
      int count = 0;

      for (String key : sortedKeys) {
        if (count != 0) {
          sbStringToSign.append(StringHelper.SEPARATOR);
        }
        sbStringToSign.append(percentEncode(key)).append("=")
            .append(percentEncode(parameters.get(key)));
        count++;
      }

      String strToSign = sbStringToSign.toString();
      signature = calculateSignature(secretKey, strToSign);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return signature;
  }

  private static String calculateSignature(String key, String stringToSign) {
    byte[] signData = new byte[]{};
    try {
      Mac mac = Mac.getInstance(ALGORITHM_HMACSHA256);
      mac.init(new SecretKeySpec(key.getBytes(DEFAULT_ENCODING), ALGORITHM_HMACSHA256));
      signData = mac.doFinal(stringToSign.getBytes(DEFAULT_ENCODING));
    } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | IllegalStateException e) {
      e.printStackTrace();
    }
    return new String(Base64.encodeBase64(signData));
  }

  private static String percentEncode(String value)
      throws UnsupportedEncodingException {
    return value != null ? URLEncoder.encode(value, DEFAULT_ENCODING)
        .replace("+", "%20").replace("*", "%2A").replace("%7E", "~")
        : null;
  }

  /**
   * Converts a map to URL- encoded content. This is a convenience method
   * which can be used in combination . It makes it easy to convert
   * parameters to submit a string:
   *
   * <code>
   * key=value&key1=value1
   * </code>
   *
   * @param params map with keys and values to be posted. This map is used to
   *               build content to be posted, such that keys are names of
   *               parameters, and values are values of those posted parameters.
   *               This method will also URL-encode keys and content using UTF-8
   *               encoding.
   *               <p>
   *               String representations of both keys and values are used.
   *               </p>
   * @return object.
   */
  public static String map2QueryString(Map<?, ?> params) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      Set<?> keySet = params.keySet();
      Object[] keys = keySet.toArray();

      for (int i = 0; i < keys.length; i++) {
        stringBuilder
            .append(encode(keys[i].toString(), "UTF-8"))
            .append('=')
            .append(encode(params.get(keys[i]).toString(), "UTF-8"));
        if (i < (keys.length - 1)) {
          stringBuilder.append('&');
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("failed to generate content from map", e);
    }
    return stringBuilder.toString();
  }


}

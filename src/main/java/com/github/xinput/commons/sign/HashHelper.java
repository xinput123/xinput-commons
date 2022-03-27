package com.github.xinput.commons.sign;

/**
 * Hash 工具
 *
 * @author <a href="mailto:wuzhiqiang@ucfgroup.com">wuzhiqiang</a>
 * @version Revision: 1.0
 * @date 16/5/12 下午5:09
 */
public class HashHelper {
  private static final String FIXED_SALT = "Complexity is the path to the dark side.";

  /**
   * hash password
   *
   * @param password
   * @param salt
   * @return
   */
  public static String hashPassword(String password, String salt) {
    String hashed = BCrypt.hashpw(password, salt);
    return hashed;
  }


  public static boolean checkPassword(String password, String hashed) {
    return BCrypt.checkpw(password, hashed);
  }

  /**
   * get random salt
   *
   * @return
   */
  public static String getSalt() {
    return BCrypt.gensalt();
  }

}

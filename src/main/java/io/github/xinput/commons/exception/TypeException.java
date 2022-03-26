package io.github.xinput.commons.exception;

/**
 * 类型转换异常.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 */
public class TypeException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public TypeException() {
  }

  public TypeException(String message) {
    super(message);
  }

  public TypeException(Throwable cause) {
    super(cause);
  }

  public TypeException(String message, Throwable cause) {
    super(message, cause);
  }

}

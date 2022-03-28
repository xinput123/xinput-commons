package com.github.xinput.commons.exception;

/**
 * 校验参数错误
 *
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @since
 */
public class ValidateException extends RuntimeException {
  private String code;
  private String msg;

  /**
   * @param code 异常码
   * @param msg  异常消息
   */
  public ValidateException(String code, String msg) {
    super(msg);
    this.setCode(code);
    this.setMsg(msg);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}

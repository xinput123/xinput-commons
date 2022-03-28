package com.github.xinput.commons.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 2018年03月， Oracle 决定把 JavaEE 移交给开源组织 Eclipse 基金会，并且不再使用Java EE这个名称。
 *
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @since
 */
public class UserLoginRequest {
  @NotNull(message = "[接口版本号]不能为空！")
  @Pattern(regexp = "^(\\d+)(\\.\\d+)?$", message = "[接口版本号]格式错误！")
  @Size(min = 0, max = 4, message = "[接口版本号]长度错误,最大长度为[{max}]！")
  private String versionNo;

  @NotNull(message = "[手机号]不能为空！")
  @Size(min = 0, max = 11, message = "[用户手机号码]长度错误,最大长度为[{max}]！")
  private String mobile;

  @NotNull(message = "[订单号]不能为空！")
  @Size(min = 0, max = 32, message = "[订单号]长度错误,最大长度为[{max}]！")
  private String orderId;

  @NotNull(message = "[验证码]不能为空！")
  @Size(min = 0, max = 6, message = "[验证码]长度错误,最大长度为[{max}]！")
  private String smsCode;

  public String getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(String versionNo) {
    this.versionNo = versionNo;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getSmsCode() {
    return smsCode;
  }

  public void setSmsCode(String smsCode) {
    this.smsCode = smsCode;
  }
}

package com.github.xinput.commons;

import com.github.xinput.commons.domain.UserLoginRequest;
import com.github.xinput.commons.exception.ValidateException;

/**
 * 校验类测试
 *
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @since
 */
public class ValidateDemo {

  public static void main(String[] args) {
    UserLoginRequest request = null;
    validate(request);

    request = new UserLoginRequest();
    System.out.println("==========");
    validate(request);

    // 版本号输入错误
    System.out.println("==========");
    request.setVersionNo("abc");
    validate(request);

    // 订单号为空
    System.out.println("==========");
    request.setVersionNo("1234");
    request.setOrderId("");
    validate(request);

    // 手机号长度有误
    System.out.println("==========");
    request.setOrderId("13134132414");
    request.setMobile("138013800000");
    validate(request);
  }

  private static void validate(UserLoginRequest request) {
    try {
      ValidateHelper.validate(request);
    } catch (ValidateException e) {
      //TODO 在实际业务中直接获取异常码、异常信息进行包装返回给前端即可
      System.out.println(String.format("code: %s, msg:%s", e.getCode(), e.getMsg()));
    }
  }
}

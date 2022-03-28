package com.github.xinput.commons;

import com.github.xinput.commons.exception.ValidateException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * 参数校验
 *
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @since
 */
public class ValidateHelper {

  public static <T> void validate(T t) {
    if (null == t) {
      throw new ValidateException("10001", "参数为空");
    }

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

    for (ConstraintViolation<T> constraintViolation : constraintViolations) {
      if (StringUtils.isNotEmpty(constraintViolation.getMessage())) {
        throw new ValidateException("99999", constraintViolation.getMessage());
      }
    }
  }
}

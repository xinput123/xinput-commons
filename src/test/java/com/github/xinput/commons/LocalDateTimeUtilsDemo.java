package com.github.xinput.commons;

import com.github.xinput.commons.date.LocalDateTimeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class LocalDateTimeUtilsDemo {

  @Test
  public void betweenAndIn() {
    LocalDateTime date = null, t2 = null, t3 = null;
    Assert.assertFalse(LocalDateTimeUtils.between(date, t2, t3));
    Assert.assertFalse(LocalDateTimeUtils.in(date, t2, t3));

    date = LocalDateTime.of(2021, 1, 1, 1, 1, 1);
    t2 = LocalDateTime.of(2021, 1, 1, 1, 1, 1);
    Assert.assertFalse(LocalDateTimeUtils.between(date, t2, t3));
    Assert.assertFalse(LocalDateTimeUtils.in(date, t2, t3));

    t3 = LocalDateTime.of(2021, 1, 1, 1, 1, 1);
    Assert.assertFalse(LocalDateTimeUtils.between(date, t2, t3));
    Assert.assertFalse(LocalDateTimeUtils.in(date, t2, t3));

    t3 = LocalDateTime.of(2021, 1, 1, 1, 1, 2);
    Assert.assertFalse(LocalDateTimeUtils.between(date, t2, t3));
    Assert.assertTrue(LocalDateTimeUtils.in(date, t2, t3));

    t2 = LocalDateTime.of(2021, 1, 1, 1, 1, 0);
    Assert.assertTrue(LocalDateTimeUtils.between(date, t2, t3));
    Assert.assertTrue(LocalDateTimeUtils.in(date, t2, t3));
  }
}

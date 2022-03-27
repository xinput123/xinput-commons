package com.github.xinput.commons;

import com.github.xinput.commons.date.LocalTimeUtils;
import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @date 2020-09-03 16:25
 */
public class LocalTimeUtilsDemo {

  @Test
  public void format() {
    LocalTime now = LocalTime.now();
    System.out.println(LocalTimeUtils.formatTime(now));
    System.out.println(LocalTimeUtils.format(now, DateTimeFormatter.ofPattern("HH:mm")));

    System.out.println(LocalTimeUtils.asMillSecond(System.currentTimeMillis()));
    System.out.println(LocalTimeUtils.asSecond(System.currentTimeMillis() / 1000L));
  }

  @Test
  public void parse() {
    System.out.println(LocalTimeUtils.parseTime("18:43:20"));
    System.out.println(LocalTimeUtils.parseTime("18:43", DateTimeFormatter.ofPattern("HH:mm")));
  }
}

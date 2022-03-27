package com.github.xinput.commons;

import com.github.xinput.commons.date.LocalDateUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @date 2020-09-03 16:17
 */
public class LocalDateUtilsDemo {

  @Test
  public void format() {
    LocalDate localDate = LocalDate.now();
    System.out.println(LocalDateUtils.formatDate(localDate));
    System.out.println(LocalDateUtils.format(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    System.out.println(LocalDateUtils.asMillSecond(System.currentTimeMillis()));
    System.out.println(LocalDateUtils.asSecond(System.currentTimeMillis() / 1000L));
  }

  @Test
  public void parse() {
    System.out.println(LocalDateUtils.parseDate("2020-09-03"));
    final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    System.out.println(LocalDateUtils.parse("2020-09-03 10:20:30", DATE_FORMATTER));
  }

  @Test
  public void getMiddleLocalDate() {
    LocalDate begin = LocalDate.of(2019, 12, 10);
    LocalDate end = LocalDate.of(2020, 1, 10);

    List<LocalDate> middleLocalDates = LocalDateUtils.getMiddleLocalDate(begin, end);
    for (LocalDate middleLocalDate : middleLocalDates) {
      System.out.println(middleLocalDate.toString());
    }

    System.out.println(begin.atStartOfDay());
  }
}

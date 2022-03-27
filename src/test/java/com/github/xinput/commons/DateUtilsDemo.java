package com.github.xinput.commons;

import com.github.xinput.commons.date.DateUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @date 2020-09-03 16:06
 */
public class DateUtilsDemo {

  @Test
  public void format() throws InterruptedException {
    Date date = new Date();
    ExecutorService executorService = Executors.newFixedThreadPool(500);
    for (int i = 0; i < 500; i++) {
      executorService.execute(new Runnable() {
        @Override
        public void run() {
          for (int i = 0; i < 10000; i++) {
            System.out.println(DateUtils.formatDateTime(date));
            System.out.println(DateUtils.formatDate(date));
            System.out.println(DateUtils.formatTime(date));
          }
        }
      });
    }
    Thread.sleep(3000000);
  }

  @Test
  public void parse() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(500);
    for (int i = 0; i < 500; i++) {
      executorService.execute(new Runnable() {
        @Override
        public void run() {
          for (int i = 0; i < 10000; i++) {
            System.out.println(DateUtils.parseDateTime("2020-09-03 16:54:05"));
            System.out.println(DateUtils.parseDate("2020-09-03"));
            System.out.println(DateUtils.parseTime("16:54:05"));
          }
        }
      });
    }
    Thread.sleep(3000000);
  }

  @Test
  public void as() {
    System.out.println(DateUtils.formatDateTime(DateUtils.as(LocalDateTime.now())));
    System.out.println(DateUtils.formatDateTime(DateUtils.as(LocalDate.now())));
  }
}

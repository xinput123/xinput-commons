package com.github.xinput.commons;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.xinput.bleach.util.http.HttpUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @date 2020-09-10 19:26
 */
public class HttpUtilsDemo {

  private static Joiner.MapJoiner joiner = Joiner.on("&").withKeyValueSeparator("=");

  @Test
  public void url() {
    String url = "http://www.baidu.com";
    Map<String, Object> params = Maps.newHashMap();
    params.put("offset", "0");
    params.put("limit", "10");
    Assert.assertEquals("http://www.baidu.com?offset=0&limit=10", HttpUtils.getUrl(url, params));

    url = "http://www.baidu.com?";
    Assert.assertEquals("http://www.baidu.com?offset=0&limit=10", HttpUtils.getUrl(url, params));

    url = "http://www.baidu.com?key=1";
    Assert.assertEquals("http://www.baidu.com?key=1&offset=0&limit=10", HttpUtils.getUrl(url, params));

    url = "http://www.baidu.com?key=1";
    Assert.assertEquals("http://www.baidu.com?key=1&offset=0&limit=10", HttpUtils.getUrl(url, params));

    url = "http://www.baidu.com?name=xinput&";
    Assert.assertEquals("http://www.baidu.com?name=xinput&offset=0&limit=10", HttpUtils.getUrl(url, params));

    Assert.assertEquals(HttpUtils.validUrl("http"), false);
    Assert.assertEquals(HttpUtils.validUrl("https"), false);
    Assert.assertEquals(HttpUtils.validUrl("http:"), false);
    Assert.assertEquals(HttpUtils.validUrl("https:"), false);
    Assert.assertEquals(HttpUtils.validUrl("http://"), true);
    Assert.assertEquals(HttpUtils.validUrl("https://"), true);
  }

  @Test
  public void testGet() {
    String url = "https://d-doctor.dr-r.cn/api/users";
//        url = "http://localhost:9099/api/status";
//        url = "http://localhost:9099/api/v1/users";
//        url = "https://console.tim.qq.com/v4/openim/querystate";
    try {
      String message = HttpUtils.get(url);
      System.out.println(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

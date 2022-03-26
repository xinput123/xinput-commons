package io.github.xinput.commons.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 带连接池的HTTP工具，支持get/post，及指定编码。
 *
 * @author yunjie.du
 */
public class SimpleHttpUtils {

  private static final Logger logger = LoggerFactory.getLogger(SimpleHttpUtils.class);

  private static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

  /**
   * GET方式获取url数据 返回数据编码使用自动探测
   */
  public static String get(String url) throws IOException {
    return get(url, null);
  }

  /**
   * get方式获取url数据
   *
   * @param url
   * @param charset 对方url页面的编码，不为null可以免去自动探测的性能消耗，如果为null，则使用自动探测
   */
  public static String get(String url, String charset) throws IOException {
    HttpGet get = new HttpGet(url);
    return execute(get, null, charset);
  }

  /**
   * post数据到url 返回数据编码使用自动探测
   *
   * @param nvps 参数
   */
  public static String post(String url, List<NameValuePair> nvps) throws IOException {
    return post(url, nvps, null);
  }

  /**
   * post数据到url
   *
   * @param url
   * @param nvps    参数
   * @param charset 对方url页面的编码，不为null可以免去自动探测的性能消耗，如果为null，则使用自动探测
   */
  public static String post(String url, List<NameValuePair> nvps, String charset) throws IOException {
    HttpPost httpost = new HttpPost(url);
    try {
      httpost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8.toString()));
    } catch (UnsupportedEncodingException e1) {
      logger.warn("httpPost parameter UrlEncodedFormEntity UnsupportedEncodingException:" + e1.getMessage());
      return null;
    }
    return execute(httpost, null, charset);
  }

  private static String execute(HttpUriRequest request, HttpContext context, String charset) throws IOException {
    String content = null;
    HttpEntity entity = null;
    try {
      HttpResponse response = HTTP_CLIENT.execute(request, context);
      StatusLine status = response.getStatusLine();
      entity = response.getEntity();

      if (status != null && status.getStatusCode() == HttpStatus.SC_OK) {
        content = EntityUtils.toString(entity, charset);
      } else {
        content = EntityUtils.toString(entity, charset);
        logger.error("http execute url error. url:[{}]. status:[{}], content:[{}].", request.getURI(), status, content);
      }
    } finally {
      // 确保释放连接
      EntityUtils.consume(entity);
    }
    return content;
  }

}


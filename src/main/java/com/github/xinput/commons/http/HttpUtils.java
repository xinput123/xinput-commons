package com.github.xinput.commons.http;

import com.github.xinput.commons.JsonHelper;
import com.github.xinput.commons.StringHelper;
import com.github.xinput.commons.exception.HttpUtilException;
import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 基于HttpClient实现的Http请求工具
 */
public class HttpUtils {

  private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

  private static volatile CloseableHttpClient instance;

  /**
   * 连接池
   */
  private static PoolingHttpClientConnectionManager CONN_MANAGER;

  private static RequestConfig REQUEST_CONFIG;

  /**
   * 监控连接间隔
   */
  private static final long RELEASE_CONNECTION_WAIT_TIME = 5000;

  private static LaxRedirectStrategy redirectStrategy = null;

  private static HttpRequestRetryHandler myRetryHandler = null;

  private static SSLConnectionSocketFactory sslConnectionSocketFactory = null;

  /**
   * 封装请求参数
   */
  private static Joiner.MapJoiner joiner = Joiner.on("&").withKeyValueSeparator("=");

  static {
    initHttpClient();
    // 启动清理连接池链接线程
    Thread thread = new IdleConnectionMonitorThread(CONN_MANAGER);
    thread.setName("bleach_idle_thread");
    thread.setDaemon(true);
    thread.start();
  }

  public static void initHttpClient() {
    REQUEST_CONFIG = RequestConfig.custom()
        .setConnectTimeout(3000)
        .setSocketTimeout(3000)
        // 忽略cookie,如果不需要登陆最好去掉,否则修改策略保存cookie即可
        .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
        .setConnectionRequestTimeout(6000)
        //设置代理ip,不设置就用本机
        // .setProxy(ip)
        .build();

    // 重定向策略初始化
    redirectStrategy = new LaxRedirectStrategy();

    // 请求重试机制，默认重试3次
    myRetryHandler = (exception, executionCount, context) -> {
      if (executionCount >= 3) {
        return false;
      }
      if (exception instanceof InterruptedIOException) {
        return false;
      }
      if (exception instanceof UnknownHostException) {
        return false;
      }
      if (exception instanceof ConnectTimeoutException) {
        return false;
      }
      if (exception instanceof SSLException) {
        // SSL handshake exception
        return false;
      }
      HttpClientContext clientContext = HttpClientContext.adapt(context);
      HttpRequest request = clientContext.getRequest();
      boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
      if (idempotent) {
        // Retry if the request is considered idempotent
        return true;
      }
      return false;
    };

    try {
      SSLContextBuilder builder = new SSLContextBuilder();
      builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
      sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
    } catch (Exception e) {
      logger.error("初始化httpclient连接池失败.", e);
    }

    Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", new PlainConnectionSocketFactory())
        .register("https", sslConnectionSocketFactory)
        .build();
    // 创建httpclient连接池
    CONN_MANAGER = new PoolingHttpClientConnectionManager(registry);
    // 设置连接池最大数量,这个参数表示所有连接最大数。
    CONN_MANAGER.setMaxTotal(200);
    // 设置单个路由最大连接数量，表示单个域名的最大连接数，
    // 例如:www.baidu.com.www.google.com表示不同的域名,则连接统一域名下的资源的最大连接数就是该参数,总和是上面的参数。
    CONN_MANAGER.setDefaultMaxPerRoute(100);
  }

  public static CloseableHttpClient getInstance() {
    if (null == instance) {
      synchronized (CloseableHttpClient.class) {
        if (null == instance) {
          instance = HttpClients.custom()
              .setSSLSocketFactory(sslConnectionSocketFactory)
              .setConnectionManager(CONN_MANAGER)
              .setDefaultRequestConfig(REQUEST_CONFIG)
              .setRedirectStrategy(redirectStrategy)
              .setRetryHandler(myRetryHandler)
              .build();
        }
      }
    }
    return instance;
  }

  public static String get(String url) throws IOException {
    return get(url, null, null, null);
  }

  public static String get(String url, Map<String, Object> params) throws IOException {
    return get(url, params, null, null);
  }

  public static String get(String url, List<Map<String, String>> headers) throws IOException {
    return get(url, null, headers, null);
  }

  public static String get(String url, Map<String, Object> params, List<Map<String, String>> headers) throws IOException {
    return get(url, params, headers, null);
  }

  public static String get(String url, List<Map<String, String>> headers, Charset charset) throws IOException {
    return get(url, null, headers, charset);
  }

  /**
   * get请求
   *
   * @param url     请求的url地址
   * @param params  请求参数,拼接在url后面
   * @param headers header请求头信息
   * @param charset 默认编解码
   */
  public static String get(String url, Map<String, Object> params, List<Map<String, String>> headers, Charset charset) throws IOException {
    if (charset == null) {
      charset = StandardCharsets.UTF_8;
    }

    String urlString = getUrl(url, params);
    if (!validUrl(urlString)) {
      throw new HttpUtilException(StringHelper.join("url不符合规范,url : ", urlString));
    }
    HttpGet httpGet = new HttpGet(urlString);
    setCommonHeaders(httpGet, headers);

    return execute(httpGet, charset);
  }

  public static String post(String url) throws IOException {
    return post(url, null, null, null);
  }

  public static String post(String url, Object params) throws IOException {
    return post(url, params, null, null);
  }

  public static String post(String url, List<Map<String, String>> headers) throws IOException {
    return post(url, headers, null);
  }

  public static String post(String url, Object params, List<Map<String, String>> headers) throws IOException {
    return post(url, params, headers, null);
  }

  public static String post(String url, Object params, List<Map<String, String>> headers, Charset charset) throws IOException {
    if (charset == null) {
      charset = StandardCharsets.UTF_8;
    }

    if (validUrl(url)) {
      throw new HttpUtilException(StringHelper.join("url不符合规范,url : ", url));
    }
    HttpPost httpPost = new HttpPost(url);
    setCommonHeaders(httpPost, headers);
    httpPost.setEntity(new StringEntity(JsonHelper.toJsonString(params), charset));

    return execute(httpPost, charset);
  }

  public static String put(String url) throws IOException {
    return put(url, null, null, null);
  }

  public static String put(String url, Object params) throws IOException {
    return put(url, params, null, null);
  }

  public static String put(String url, List<Map<String, String>> headers) throws IOException {
    return put(url, headers, null);
  }

  public static String put(String url, Object params, List<Map<String, String>> headers) throws IOException {
    return put(url, params, headers, null);
  }

  /**
   * put方式 请求
   *
   * @param url     请求地址
   * @param params  body中参数
   * @param headers 自定义header
   * @param charset 编码格式
   * @throws IOException
   */
  public static String put(String url, Object params, List<Map<String, String>> headers, Charset charset) throws IOException {
    if (charset == null) {
      charset = StandardCharsets.UTF_8;
    }

    if (validUrl(url)) {
      throw new HttpUtilException(StringHelper.join("url不符合规范,url : ", url));
    }

    HttpPut httpPut = new HttpPut(url);
    setCommonHeaders(httpPut, headers);
    httpPut.setEntity(new StringEntity(JsonHelper.toJsonString(params), charset));

    return execute(httpPut, charset);
  }

  public static String delete(String url) throws IOException {
    return delete(url, null, null, null);
  }

  public static String delete(String url, Map<String, Object> params) throws IOException {
    return delete(url, params, null, null);
  }

  public static String delete(String url, List<Map<String, String>> headers) throws IOException {
    return delete(url, null, headers, null);
  }

  public static String delete(String url, Map<String, Object> params, List<Map<String, String>> headers) throws IOException {
    return delete(url, params, headers, null);
  }

  public static String delete(String url, List<Map<String, String>> headers, Charset charset) throws IOException {
    return delete(url, null, headers, charset);
  }

  /**
   * delete方式 请求
   *
   * @param url     请求地址
   * @param params  url后参数
   * @param headers 自定义header
   * @param charset 编码格式
   */
  public static String delete(String url, Map<String, Object> params, List<Map<String, String>> headers, Charset charset) throws IOException {
    if (charset == null) {
      charset = StandardCharsets.UTF_8;
    }

    String urlString = getUrl(url, params);
    if (!validUrl(urlString)) {
      throw new HttpUtilException(StringHelper.join("url不符合规范,url : ", urlString));
    }
    HttpDelete httpDelete = new HttpDelete(urlString);
    setCommonHeaders(httpDelete, headers);

    return execute(httpDelete, charset);
  }

  private static String execute(HttpUriRequest request, Charset charset) throws IOException {
    String content;
    HttpEntity entity = null;
    try {
      HttpResponse response = getInstance().execute(request);
      int statusCode = response.getStatusLine().getStatusCode();
      logger.info("REQUEST URI:[{}], STATUS CODE : [{}]. ", request.getURI(), statusCode);
      // only for record log
      if (HttpStatus.SC_OK == statusCode) {
        logger.info("REQUEST URI:[{}] SUCCESS. ", request.getURI());
      } else {
        logger.error("REQUEST URI:[{}] ERROR. ERROR CODE : [{}].", request.getURI(), statusCode);
      }
      entity = response.getEntity();
      content = EntityUtils.toString(entity, charset);
    } catch (IOException e) {
      logger.error("NETWORK ERROR. URL : [{}].", request.getURI(), e);
      throw new HttpUtilException("NETWORK ERROR. URL: " + request.getURI(), e);
    } finally {
      // 确保释放连接
      EntityUtils.consume(entity);
    }

    return content;
  }

  public static boolean validUrl(String url) {
    if (StringHelper.isBlank(url)
        || !StringHelper.startsWithAny(url.toLowerCase(), "http://", "https://")) {
      return false;
    }

    return true;
  }

  public static String getUrl(String url, Map<String, Object> params) {
    if (StringHelper.isBlank(url)) {
      return StringHelper.EMPTY;
    }

    if (MapUtils.isEmpty(params)) {
      return url;
    }

    String queryString = joiner.join(params);
    // url=http://www.baidu.com
    if (url.indexOf("?") < 0) {
      return StringHelper.join(url, "?", queryString);
    }

    // url=http://www.baidu.com?
    // url=http://www.baidu.com?name=xinput&
    if (url.endsWith("?") || url.endsWith("&")) {
      return StringHelper.join(url, queryString);
    }

    // url=http://www.baidu.com?name=123
    return StringHelper.join(url, "&", queryString);
  }

  private static void setCommonHeaders(AbstractHttpMessage request) {
    request.addHeader("Content-type", "application/json;charset=utf-8");
    request.setHeader("Accept", "application/json");
  }

  private static void setCommonHeaders(AbstractHttpMessage request, List<Map<String, String>> headers) {
    setCommonHeaders(request);
    // 额外的header
    if (CollectionUtils.isNotEmpty(headers)) {
      for (Map<String, String> header : headers) {
        header.forEach((key, value) -> {
          if (request.containsHeader(key)) {
            request.setHeader(key, value);
          } else {
            request.addHeader(key, value);
          }
        });
      }
    }
  }
}
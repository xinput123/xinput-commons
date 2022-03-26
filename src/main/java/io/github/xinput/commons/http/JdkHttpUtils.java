package io.github.xinput.commons.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Jdk提供的http请求
 * 建议使用 HttpUtils 或者 SimpleHttpUtils
 */
public class JdkHttpUtils {

  private static final Logger log = LoggerFactory.getLogger(JdkHttpUtils.class);

  public static String sendGet(String url) {
    StringBuilder result = new StringBuilder();
    BufferedReader in = null;
    try {
      URL realUrl = new URL(url);
      // 打开和URL之间的连接
      URLConnection connection = realUrl.openConnection();
      // 设置通用的请求属性
      connection.setRequestProperty("Content-type", "application/json;charset=utf-8");
      connection.setRequestProperty("Accept", "application/json");
      // 建立实际的连接
      connection.connect();
      // 定义 BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
      String line;
      while ((line = in.readLine()) != null) {
        result.append(line);
      }
    } catch (Exception e) {
      log.error("发送 GET 请求出现异常:", e);
    }
    // 使用finally块来关闭输入流
    finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e2) {
        log.error("关闭流异常:", e2);
      }
    }
    return result.toString();
  }

  public static String sendPostUrl(String url, String param) {
    PrintWriter out = null;
    BufferedReader in = null;
    StringBuilder result = new StringBuilder();
    try {
      URL realUrl = new URL(url);
      // 打开和URL之间的连接
      URLConnection conn = realUrl.openConnection();
      // 设置通用的请求属性
      // 发送POST请求必须设置如下两行
      conn.setRequestProperty("Content-type", "application/json;charset=utf-8");
      conn.setRequestProperty("Accept", "application/json");
      conn.setDoOutput(true);
      conn.setDoInput(true);
      // 获取URLConnection对象对应的输出流
      out = new PrintWriter(conn.getOutputStream());
      // 发送请求参数
      if (null != param) {
        out.print(param);
      }
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(
          conn.getInputStream(), StandardCharsets.UTF_8));
      String line;
      while ((line = in.readLine()) != null) {
        result.append(line);
      }
    } catch (Exception e) {
      log.error("发送 POST 请求出现异常:", e);
    }
    // 使用finally块来关闭输出流、输入流
    finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        log.error("关闭流异常:", ex);
      }
    }
    return result.toString();
  }

  public static String sendPost(String url, Map<String, String> param) {
    PrintWriter out = null;
    BufferedReader in = null;
    StringBuilder result = new StringBuilder();
    StringBuffer buffer = new StringBuffer();
    try {
      if (param != null && !param.isEmpty()) {
        for (Map.Entry<String, String> entry : param.entrySet()) {
          buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), String.valueOf(StandardCharsets.UTF_8))).append("&");
        }
        buffer.deleteCharAt(buffer.length() - 1);
      }
      URL realUrl = new URL(url);
      // 打开和URL之间的连接
      URLConnection conn = realUrl.openConnection();
      // 设置通用的请求属性
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // 发送POST请求必须设置如下两行
      conn.setDoOutput(true);
      conn.setDoInput(true);
      // 获取URLConnection对象对应的输出流
      out = new PrintWriter(conn.getOutputStream());
      // 发送请求参数
      out.print(buffer);
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
      String line;
      while ((line = in.readLine()) != null) {
        result.append(line);
      }
    } catch (Exception e) {
      log.error("发送 POST 请求出现异常:", e);
    }
    // 使用finally块来关闭输出流、输入流
    finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        log.error("关闭流异常:", ex);
      }
    }
    return result.toString();
  }
}
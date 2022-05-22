package com.github.xinput.commons.file;

import com.github.xinput.commons.JsonHelper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 */
public class FileHelper {

  private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

  /**
   * 读取json文件
   *
   * @param jsonFileName 文件名
   * @param clazz        类型
   * @throws IOException
   */
  public static <T> T readFileBean(String jsonFileName, Class<T> clazz) throws IOException {
    String demoTaskString = Files.asCharSource(new File(jsonFileName), Charsets.UTF_8).read();
    return JsonHelper.toBean(demoTaskString, clazz);
  }

  public static <T> List<T> readFileList(String jsonFileName, Class<T> clazz) throws IOException {
    String demoTaskString = Files.asCharSource(new File(jsonFileName), Charsets.UTF_8).read();
    return JsonHelper.toList(demoTaskString, clazz);
  }

  /**
   * 按行读取文件
   */
  public static List<String> readFile(String fileName) {
    return readFile(fileName, 100);
  }

  /**
   * 按行读取文件
   *
   * @param fileName 文件名
   * @param size     内容行数
   */
  public static List<String> readFile(String fileName, int size) {
    List<String> lists = Lists.newArrayListWithCapacity(size);
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line = reader.readLine();
      while (line != null) {
        lists.add(line);
        line = reader.readLine();
      }
    } catch (IOException e) {
      logger.error("readFile error. fileName:{}.", fileName, e);
    }
    return lists;
  }

  /**
   * 将csv文件内容转为实体对象
   */
  public static <T> List<T> readCsv(String csvFileName, Class<T> clazz) {
    return readCsv(csvFileName, 0, ",", clazz);
  }

  public static <T> List<T> readCsv(String csvFileName, int skipRowNum, Class<T> clazz) {
    return readCsv(csvFileName, skipRowNum, ",", clazz);
  }

  public static <T> List<T> readCsv(String csvFileName, String delimiter, Class<T> clazz) {
    return readCsv(csvFileName, 0, delimiter, clazz);
  }

  /**
   * 将csv文件内容转为实体对象
   *
   * @param csvFileName
   * @param skipRowNum  跳过多少行
   * @param delimiter   分隔符
   */
  public static <T> List<T> readCsv(String csvFileName, int skipRowNum, String delimiter, Class<T> clazz) {
    BeanListProcessor<T> rowProcessor = new BeanListProcessor<>(clazz);
    CsvParserSettings parserSettings = new CsvParserSettings();
    parserSettings.setProcessor(rowProcessor);
    parserSettings.getFormat().setLineSeparator("\n");
    parserSettings.getFormat().setDelimiter(delimiter);
    parserSettings.setNumberOfRowsToSkip(skipRowNum);

    CsvParser parser = new CsvParser(parserSettings);
    parser.parse(new File(csvFileName), "UTF-8");
    return rowProcessor.getBeans();
  }


  /**
   * 将list集合文件写入文本
   *
   * @param lists
   * @param file
   * @return
   * @throws Exception
   */
  public static void writeFile(List<String> lists, String file) throws Exception {
    File writeFile = new File(file);

    try (// 写入中文时解决中文乱码问题
         FileOutputStream fos = new FileOutputStream(writeFile);
         OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
         BufferedWriter bw = new BufferedWriter(osw)) {
      for (String message : lists) {
        bw.write(message);
        bw.newLine(); // 换行
      }
      bw.flush();
    } catch (Exception e) {
      logger.error("writeFile error. file:{}.", file, e);
      throw e;
    }
  }
}

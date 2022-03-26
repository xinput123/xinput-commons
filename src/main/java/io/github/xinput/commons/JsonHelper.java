package io.github.xinput.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.github.xinput.commons.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author yuan.lai
 * @since
 */
public class JsonHelper {
  private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);

  private static ObjectMapper mapper = new ObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      // 指定时区
      .setTimeZone(TimeZone.getDefault())
      // 日期类型字符串处理
      .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
      .registerModule(
          // java8日期处理
          new JavaTimeModule()
              .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtils.DATE_TIME_FORMATTER))
              .addSerializer(LocalDate.class, new LocalDateSerializer(DateUtils.DATE_FORMATTER))
              .addSerializer(LocalTime.class, new LocalTimeSerializer(DateUtils.TIME_FORMATTER))
              .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtils.DATE_TIME_FORMATTER))
              .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateUtils.DATE_FORMATTER))
              .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateUtils.TIME_FORMATTER))
      );

  public static String toJsonString(Object obj) {
    if (null == obj) {
      return null;
    }

    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      logger.error("obj to json JsonProcessingException. ", e);
      return null;
    }
  }

  public static String toJsonString(Object obj, boolean prettyFormat) {
    if (null == obj) {
      return null;
    }

    if (prettyFormat) {
      try {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
      } catch (JsonProcessingException e) {
        logger.error("obj to json JsonProcessingException. prettyFormat:{}. ", prettyFormat, e);
        return null;
      }
    }
    return toJsonString(obj);
  }

  /**
   * 将对象字符串(不是List格式),转化成对象.
   */
  @SuppressWarnings("unchecked")
  public static <T> T toBean(String content, Class<T> clazz) {
    if (StringHelper.isEmpty(content)) {
      return null;
    }

    T t = null;
    try {
      t = mapper.readValue(content, clazz);
    } catch (JsonParseException e) {
      logger.error("content toBean JsonParseException. content:{}.", content, e);
    } catch (JsonMappingException e) {
      logger.error("content toBean JsonMappingException. content:{}.", content, e);
    } catch (IOException e) {
      logger.error("content toBean IOException. content:{}.", content, e);
    }
    return t;
  }

  /**
   * 将对象reader(不是List格式),转化成对象.
   */
  @SuppressWarnings("unchecked")
  public static <T> T toBean(Reader reader, Class<T> clazz) {
    if (reader == null) {
      return null;
    }

    T t = null;
    try {
      t = mapper.readValue(reader, clazz);
    } catch (JsonParseException e) {
      logger.error("Reader to bean JsonParseException. ", e);
    } catch (JsonMappingException e) {
      logger.error("Reader to bean JsonMappingException. ", e);
    } catch (IOException e) {
      logger.error("Reader to bean IOException. ", e);
    }
    return t;
  }

  /**
   * 将Json转为map
   */
  public static <V> Map<String, V> toMap(String json, Class<V> valueClazz) {
    JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, valueClazz);
    try {
      return mapper.readValue(json, jvt);
    } catch (JsonProcessingException e) {
      logger.error("json toMap JsonProcessingException. ", e);
      return new HashMap();
    }
  }

  /**
   * 将Json转为map
   */
  public static <K, V> Map<K, V> toMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
    JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class, keyClazz, valueClazz);
    try {
      return mapper.readValue(json, jvt);
    } catch (JsonProcessingException e) {
      logger.error("json toMap JsonProcessingException. ", e);
      return new HashMap();
    }
  }

  /**
   * 将 Json 转为负载类型的 Map.比如说 Map<String, Map<String, String>> 格式
   *
   * @param typeReference new TypeReference<HashMap<Integer, Map<Integer, String>>>() {};
   */
  public static <T> T parseObject(String json, TypeReference<T> typeReference) {
    try {
      return mapper.readValue(json, typeReference);
    } catch (JsonProcessingException e) {
      logger.error("to Map error. json:{}, typeReference:{}.", json, typeReference.getType(), e);
      return null;
    }
  }

  /**
   * 将对象InputStream(不是List格式),转化成对象.
   */
  @SuppressWarnings("unchecked")
  public static <T> T toBean(InputStream stream, Class<T> clazz) {
    if (stream == null) {
      return null;
    }

    T t = null;
    try {
      t = mapper.readValue(stream, clazz);
    } catch (JsonParseException e) {
      logger.error("InputStream to bean JsonParseException. ", e);
    } catch (JsonMappingException e) {
      logger.error("InputStream to bean JsonMappingException. ", e);
    } catch (IOException e) {
      logger.error("InputStream to bean IOException. ", e);
    }
    return t;
  }

  /**
   * 将List对象字符串,转化成List对象.
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> toList(String content, Class<T> clazz) {
    if (content == null) {
      return null;
    }
    return (List<T>) readValueList(content, ArrayList.class, clazz);
  }

  /**
   * 将List对象字符串,转化成List对象.
   *
   * @param content         字符串内容
   * @param collectionClass 集合类型,例如 ArrayList.class
   * @param clazz           对象类型 例如 User.class
   */
  private static Object readValueList(String content, Class collectionClass, Class clazz) {
    if (content == null) {
      return null;
    }

    Object o = null;

    try {
      o = mapper.readValue(content, getCollectionType(collectionClass, clazz));
    } catch (JsonParseException e) {
      logger.error("content readValueList JsonParseException. content:{}.", content, e);
    } catch (JsonMappingException e) {
      logger.error("content readValueList JsonMappingException. content:{}.", content, e);
    } catch (IOException e) {
      logger.error("content readValueList IOException. content:{}.", content, e);
    }

    return o;
  }

  private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
    return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
  }
}
